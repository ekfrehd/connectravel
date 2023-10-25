package org.ezone.room.service;

import lombok.RequiredArgsConstructor;
import org.ezone.room.dto.ReservationDTO;
import org.ezone.room.dto.RoomDTO;
import org.ezone.room.entity.AccommodationEntity;
import org.ezone.room.entity.Member;
import org.ezone.room.entity.ReservationEntity;
import org.ezone.room.entity.RoomEntity;
import org.ezone.room.repository.AccommodationRepository;
import org.ezone.room.repository.MemberRepository;
import org.ezone.room.repository.ReservationRepository;
import org.ezone.room.repository.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService{
    final RoomRepository repository;
    final MemberRepository memberRepository;
    final ReservationRepository reservationRepository;
    final AccommodationRepository accommodationRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Long register(ReservationDTO rvDTO, RoomDTO roomDTO, Authentication authentication) {
        List<Object[]> list = repository.findByDateByRno(rvDTO.getStartDate(), rvDTO.getEndDate(), roomDTO.getRno());
        boolean isReservationCanceled = list.stream().anyMatch(o -> o[1] != null && !((ReservationEntity) o[1]).isState());

        if ((list.get(0)[1] == null || isReservationCanceled) && rvDTO.getEndDate().compareTo(rvDTO.getStartDate()) > 0) {
           Member member =memberRepository.findByEmail(authentication.getName());

            ReservationEntity entity = ReservationEntity.builder()
                    .message(rvDTO.getMessage())
                    .money(rvDTO.getMoney())
                    .StartDate(rvDTO.getStartDate())
                    .EndDate(rvDTO.getEndDate())
                    .room_id(modelMapper.map(roomDTO, RoomEntity.class))
                    .member_id(member)
                    .build();
            AccommodationEntity accommodation = accommodationRepository.findAnoByRno(roomDTO.getRno());
            accommodation.setCount(accommodation.getCount()+1);
            member.setPoint(member.getPoint()+100);
            Long rvno = reservationRepository.save(entity).getRvno();
            return rvno;
        }
        return null;
    }

    @Override
    public List<ReservationDTO> getlist(Authentication authentication) {
        Member member = memberRepository.findByEmail(authentication.getName());
        List<ReservationDTO> dto = new ArrayList<>();
        reservationRepository.findByMember(member).forEach(i -> {
            dto.add(modelMapper.map(i,ReservationDTO.class));
        });
        return dto;
    }


    @Override
    public boolean cancel(Long rvno,Authentication authentication) {
        Member member = memberRepository.findByEmail(authentication.getName()); //인증
        Optional<ReservationEntity> Check = reservationRepository.findByRvnoAndDateAndMember_id(LocalDate.now(), rvno,member);
        //시간/예약번호/맴버 id만 해당하는 예약정보(보안상)
        //비지 않았다 = 현재 체크인 날짜보다 이전이다.
        if (!Check.isEmpty()) {
            ReservationEntity entity = Check.get();
            ReservationEntity reservation = ReservationEntity.builder().rvno(entity.getRvno()).state(false)
                    .StartDate(entity.getStartDate()).EndDate(entity.getEndDate())
                    .message(entity.getMessage()).member_id(entity.getMember_id()).room_id(entity.getRoom_id())
                    .money(entity.getMoney()).build();
            member.setPoint(member.getPoint()-100);
            Optional<AccommodationEntity> accommodation = accommodationRepository.findById(entity.getRoom_id().getAccommodationEntity().getAno());
            Optional<AccommodationEntity> accommodationOpt = accommodation;
            if (accommodationOpt.isPresent()) {
                AccommodationEntity accommodationEntity = accommodationOpt.get();
                accommodationEntity.setCount(accommodationEntity.getCount() - 1);
                accommodationRepository.save(accommodationEntity);
            }
            memberRepository.save(member);
            reservationRepository.save(reservation); //update한다.
            return true; //정상
        }
        return false; //데이터 없음. 안돼. 돌아가.
    }
}
