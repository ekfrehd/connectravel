package com.connectravel.service;

import com.connectravel.dto.*;
import com.connectravel.entity.*;
import com.connectravel.repository.*;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewBoardServiceImpl implements ReviewBoardService {

    private final ReviewBoardRepository reviewBoardRepository;
    private final ReviewReplyRepository reviewReplyRepository;
    private final ReviewReplyService reviewReplyService;
    private final MemberRepository memberRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewBoardImgRepository reviewBoardImgRepository;
    private final ModelMapper modelMapper;

    public Accommodation findAccommodationByRoomId(Long rno) {
        Room roomEntity = roomRepository.findById(rno).orElse(null);
        if (roomEntity != null) {
            return roomEntity.getAccommodation();
        }
        return null;
    }

    @Override
    public PageResultDTO<ReviewBoardDTO, ReviewBoard> getReviewBoardsAndPageInfoByAccommodationId(Long ano, PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by(Sort.Direction.DESC, "rbno");
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        Page<ReviewBoard> result = reviewBoardRepository.findByAccommodationAno(ano, pageable);

        Function<ReviewBoard, ReviewBoardDTO> fn = (reviewBoard -> {
            Member member = reviewBoard.getMember();
            List<ReviewReplyDTO> replyDTOs = reviewReplyService.getList(reviewBoard.getRbno());
            return entityToDTO(reviewBoard, replyDTOs);
        });

        return new PageResultDTO<>(result, fn);
    }

    @Transactional
    @Override
    public Long register(ReviewBoardDTO dto) throws NotFoundException {
        Optional<Member> memberOptional = memberRepository.findByEmail(dto.getWriterEmail());
        Member member = memberOptional.orElseThrow(() -> new NotFoundException("Member not found"));

        Optional<Accommodation> accommodationOptional = accommodationRepository.findById(dto.getAno());
        if (!accommodationOptional.isPresent()) {
            throw new NotFoundException("Accommodation not found");
        }
        Accommodation accommodation = accommodationOptional.get();

        Optional<Room> roomOptional = roomRepository.findById(dto.getRno());
        if (!roomOptional.isPresent()) {
            throw new NotFoundException("Room not found");
        }
        Room room = roomOptional.get();

        Optional<Reservation> reservationOptional = reservationRepository.findById(dto.getRvno());
        if (!reservationOptional.isPresent()) {
            throw new NotFoundException("Reservation not found");
        }
        Reservation reservation = reservationOptional.get();

        double currentGrade = accommodation.getGrade();
        double newGrade = dto.getGrade();
        int currentCount = accommodation.getReviewCount();

        double avarageGrade = ((currentGrade * currentCount) + newGrade) / (currentCount + 1);

        accommodation.setReviewCount(currentCount + 1);
        accommodation.setGrade(avarageGrade);
        accommodationRepository.saveAndFlush(accommodation);

        ReviewBoard reviewBoard = dtoToEntity(dto, member, reservation);
        reviewBoardRepository.save(reviewBoard);
        return reviewBoard.getRbno();
    }

    @Override
    public ReviewBoardDTO get(Long bno){

       return null;
    }

    @Transactional
    @Override
    public void modify(ReviewBoardDTO reviewBoardDTO) {
        ReviewBoard reviewBoard = reviewBoardRepository.findById(reviewBoardDTO.getRbno()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시글이 없습니다."));
        reviewBoard.changeContent(reviewBoardDTO.getContent());
        reviewBoardRepository.save(reviewBoard);
    }

    @Transactional
    @Override
    public void remove(Long rbno) {
        if (reviewReplyRepository != null) {
            reviewReplyRepository.deleteByRbno(rbno);
        }
        if (reviewBoardImgRepository != null) {
            reviewBoardImgRepository.deleteImgByRbno(rbno);
        }
        if (reviewBoardRepository != null) {
            reviewBoardRepository.deleteById(rbno);
        }
    }

    @Override
    public List<ImgDTO> getImgList(Long rbno) {
        List<ImgDTO> list = new ArrayList<>();
        ReviewBoard entity = reviewBoardRepository.findById(rbno)
                .orElseThrow(() -> new NotFoundException("ReviewBoard not found"));

        reviewBoardImgRepository.getImgByRbno(entity).forEach(i -> { //이미지는 룸을 참조하고 있다 그러니 이미지가 참조하는 룸에 해당하는 모든 이미지를 불러온다
            ImgDTO imgDTO = modelMapper.map(i,ImgDTO.class); //dto변환
            list.add(imgDTO); // list화
        });
        return list;
    }

    // DTO 객체를 Entity 객체로 변환하는 메소드
    private ReviewBoard dtoToEntity(ReviewBoardDTO dto, Member member, Reservation reservation) {
        return ReviewBoard.builder()
                .rbno(dto.getRbno())
                .content(dto.getContent())
                .grade(dto.getGrade())
                .member(member)
                .reservation(reservation)
                .build();
    }

    // Entity 객체를 DTO 객체로 변환하는 메소드
    private ReviewBoardDTO entityToDTO(ReviewBoard reviewBoard, List<ReviewReplyDTO> replyDTOs) {
        Member member = reviewBoard.getReservation().getMember();
        return ReviewBoardDTO.builder()
                .rbno(reviewBoard.getRbno())
                .rno(reviewBoard.getReservation().getRoom().getRno())
                .rvno(reviewBoard.getReservation().getRvno())
                .content(reviewBoard.getContent())
                .grade(reviewBoard.getGrade())
                .ano(reviewBoard.getReservation().getRoom().getAccommodation().getAno())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .roomName(reviewBoard.getReservation().getRoom().getRoomName())
                .replies(replyDTOs)
                .replyCount(replyDTOs.size())
                .regDate(reviewBoard.getRegTime())
                .modDate(reviewBoard.getModTime())
                .build();
    }
}