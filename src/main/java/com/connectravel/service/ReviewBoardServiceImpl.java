package com.connectravel.service;

import com.connectravel.dto.*;
import com.connectravel.entity.*;
import com.connectravel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
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

    public Accommodation getAccommodationDetailsByRoomRno(Long rno) {
        Room roomEntity = roomRepository.findById(rno).orElse(null);
        if (roomEntity != null) {
            return roomEntity.getAccommodation();
        }
        return null;
    }

    @Override
    public PageResultDTO<ReviewBoardDTO, ReviewBoard> getPaginatedReviewsByAccommodation(Long ano, PageRequestDTO pageRequestDTO) {
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
    @Override
    @Transactional
    public Long createReview(ReviewBoardDTO dto) {
        Member member = memberRepository.findByEmail(dto.getWriterEmail())
                .orElseThrow(() -> new EntityNotFoundException("Member with email " + dto.getWriterEmail() + " not found"));

        Accommodation accommodation = accommodationRepository.findById(dto.getAno())
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with id " + dto.getAno() + " not found"));

        Room room = roomRepository.findById(dto.getRno())
                .orElseThrow(() -> new EntityNotFoundException("Room with id " + dto.getRno() + " not found"));

        Reservation reservation = reservationRepository.findById(dto.getRvno())
                .orElseThrow(() -> new EntityNotFoundException("Reservation with id " + dto.getRvno() + " not found"));

        double currentGrade = accommodation.getGrade();
        double newGrade = dto.getGrade();
        int currentCount = accommodation.getReviewCount();

        double averageGrade = ((currentGrade * currentCount) + newGrade) / (currentCount + 1);

        accommodation.setReviewCount(currentCount + 1);
        accommodation.setGrade(averageGrade);
        accommodationRepository.save(accommodation);

        ReviewBoard reviewBoard = dtoToEntity(dto, member, reservation);
        reviewBoard = reviewBoardRepository.save(reviewBoard);
        return reviewBoard.getRbno();
    }


    @Override
    public ReviewBoardDTO getReviewByBno(Long bno){

       return null;
    }

    @Transactional
    @Override
    public void updateReview(ReviewBoardDTO reviewBoardDTO) {
        ReviewBoard reviewBoard = reviewBoardRepository.findById(reviewBoardDTO.getRbno()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시글이 없습니다."));
        reviewBoard.changeContent(reviewBoardDTO.getContent());
        reviewBoardRepository.save(reviewBoard);
    }

    @Transactional
    @Override
    public void deleteReview(Long rbno) {
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
    public List<ImgDTO> listReviewImages(Long rbno) {
        List<ImgDTO> list = new ArrayList<>();
        ReviewBoard entity = reviewBoardRepository.findById(rbno)
                .orElseThrow(() -> new EntityNotFoundException("ReviewBoard not found"));

        reviewBoardImgRepository.getImgByRbno(rbno).forEach(i -> {
            ImgDTO imgDTO = modelMapper.map(i, ImgDTO.class);
            list.add(imgDTO); // 리스트화
        });
        return list;
    } // 사용자가 남긴 숙소 리뷰의 이미지들을 가져오는 메서드


    private ReviewBoard dtoToEntity(ReviewBoardDTO dto, Member member, Reservation reservation) {
        return ReviewBoard.builder()
                .rbno(dto.getRbno())
                .content(dto.getContent())
                .grade(dto.getGrade())
                .member(member)
                .reservation(reservation)
                .build();
    }

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