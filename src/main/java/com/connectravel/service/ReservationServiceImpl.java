package com.connectravel.service;

import com.connectravel.dto.MemberDTO;
import com.connectravel.dto.ReservationDTO;
import com.connectravel.dto.RoomDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.Reservation;
import com.connectravel.entity.Room;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ReservationRepository;
import com.connectravel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomService roomService;
    private final MemberService memberService;

    @Override
    @Transactional
    public ReservationDTO bookRoom(ReservationDTO reservationDTO) {
        // DTO를 Entity로 변환
        Reservation reservation = dtoToEntity(reservationDTO);

        // 예약 엔티티 저장
        Reservation savedReservation = reservationRepository.save(reservation);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return entityToDTO(savedReservation);
    }
    @Override
    @Transactional(readOnly = true)
    public ReservationDTO getRoomBookingDetails(Long rvno) {
        // 예약 엔티티를 ID를 이용해 조회
        Reservation reservation = reservationRepository.findById(rvno)
                .orElseThrow(() -> new EntityNotFoundException("해당 예약을 찾을 수 없습니다. 예약 번호: " + rvno));

        // 조회된 엔티티를 DTO로 변환하여 반환
        return entityToDTO(reservation);
    }


    @Override
    @Transactional
    public ReservationDTO modifyRoomBooking(Long rvno, ReservationDTO reservationDTO) {
        // 기존 예약을 ID를 사용하여 찾기
        Reservation reservation = reservationRepository.findById(rvno)
                .orElseThrow(() -> new EntityNotFoundException("해당 예약을 찾을 수 없습니다. 예약 번호: " + rvno));

        // 날짜가 변경되었는지 확인하고 변경된 경우 새 날짜에 대한 방의 예약 가능 여부 확인
        if (!reservation.getStartDate().isEqual(reservationDTO.getStartDate()) ||
                !reservation.getEndDate().isEqual(reservationDTO.getEndDate())) {
            boolean isAvailable = checkRoomAvailability(reservation.getRoom().getRno(),
                    reservationDTO.getStartDate(),
                    reservationDTO.getEndDate());
            // 만약 새로운 날짜에 방이 예약 가능하지 않다면 예외 발생
            if (!isAvailable) {
                throw new IllegalStateException("새로운 날짜에 방이 예약 가능하지 않습니다.");
            }
        }

        // 예약 상세 정보 업데이트
        reservation.setMessage(reservationDTO.getMessage());
        reservation.setMoney(reservationDTO.getMoney());
        reservation.setNumberOfGuests(reservationDTO.getNumberOfGuests());
        reservation.setStartDate(reservationDTO.getStartDate());
        reservation.setEndDate(reservationDTO.getEndDate());
        reservation.setState(reservationDTO.isState());

        // 업데이트된 예약 저장
        Reservation updatedReservation = reservationRepository.save(reservation);

        // 업데이트된 예약을 DTO로 변환하여 반환
        return entityToDTO(updatedReservation);
    }


    @Override
    @Transactional
    public void cancelRoomBooking(Long rvno) {
        // 예약 취소 로직 구현
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> listUserRoomBookings(String userEmail) {
        // 사용자별 예약 목록 조회 로직 구현
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> listAccommodationRoomBookings(Long ano) {
        // 숙소별 예약 목록 조회 로직 구현
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> listRoomBookings(Long rno) {
        // 방별 예약 목록 조회 로직 구현
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkRoomAvailability(Long rno, LocalDate startDate, LocalDate endDate) {
        // 예약 가능 여부 확인 로직 구현
        return true;
    }

   /* @Override
    @Transactional
    public void updateRoomBookingStatus(Long rvno, ReservationStatus status) {
        // 예약 상태 변경 로직 구현
    }*/

    // Reservation 엔티티를 ReservationDTO로 변환하는 메서드
    private ReservationDTO entityToDTO(Reservation reservation) {
        RoomDTO roomDTO = roomService.entityToDTO(reservation.getRoom());
        MemberDTO memberDTO = memberService.entityToDTO(reservation.getMember()); // Member 변환

        return ReservationDTO.builder()
                .rvno(reservation.getRvno())
                .message(reservation.getMessage())
                .money(reservation.getMoney())
                .numberOfGuests(reservation.getNumberOfGuests())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .state(reservation.isState())
                .roomDTO(roomDTO) // Room 엔티티를 DTO로 변환
                .memberDTO(memberDTO) // Member 엔티티를 DTO로 변환
                .build();
    }

    // ReservationDTO를 Reservation 엔티티로 변환하는 메서드
    private Reservation dtoToEntity(ReservationDTO reservationDTO) {
        Room room = roomRepository.findById(reservationDTO.getRoomDTO().getRno())
                .orElseThrow(() -> new EntityNotFoundException("방을 찾을 수 없습니다."));
        Member member = memberRepository.findByEmail(reservationDTO.getMemberDTO().getEmail())
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        return Reservation.builder()
                .message(reservationDTO.getMessage())
                .money(reservationDTO.getMoney())
                .numberOfGuests(reservationDTO.getNumberOfGuests())
                .startDate(reservationDTO.getStartDate())
                .endDate(reservationDTO.getEndDate())
                .state(reservationDTO.isState())
                .room(room) // 여기서 Room 엔티티를 설정합니다.
                .member(member) // 여기서 Member 엔티티를 설정합니다.
                .build();
    }

}
