package org.ezone.room.service;

import java.util.List;
import javassist.NotFoundException;
import org.ezone.room.dto.ImgDTO;
import org.ezone.room.dto.PageRequestDTO;
import org.ezone.room.dto.PageResultDTO;
import org.ezone.room.dto.ReviewBoardDTO;
import org.ezone.room.dto.ReviewReplyDTO;
import org.ezone.room.entity.Accommodation;
import org.ezone.room.entity.Member;
import org.ezone.room.entity.Reservation;
import org.ezone.room.entity.ReviewBoard;

public interface ReviewBoardService {


    Long register(ReviewBoardDTO dto) throws NotFoundException; //등록

    ReviewBoardDTO get(Long bno); //조회

    void modify(ReviewBoardDTO reviewBoardDTO); //수정

    void remove(Long bno); //삭제

    PageResultDTO<ReviewBoardDTO, ReviewBoard> getReviewBoardsAndPageInfoByAccommodationId(Long ano, PageRequestDTO pageRequestDTO); // 리스트 출력

    Accommodation findAccommodationByRoomId(Long rno);

    public List<ImgDTO> getImgList(Long rbno);
    
    // DTO 객체를 Entity 객체로 변환하는 메소드
    default ReviewBoard dtoToEntity(ReviewBoardDTO dto, Member member,  Reservation reservation) {

        return ReviewBoard.builder()
                .rbno(dto.getRbno())
                .content(dto.getContent())
                .grade(dto.getGrade())
                .member(member)
                .reservation(reservation)
                .build();
    }

    // Entity 객체를 DTO 객체로 변환하는 메소드
    default ReviewBoardDTO entityToDTO(ReviewBoard reviewBoard, List<ReviewReplyDTO> replyDTOs) {

        Member member = reviewBoard.getReservation().getMember();

        return ReviewBoardDTO.builder()
                .rbno(reviewBoard.getRbno())
                .content(reviewBoard.getContent())
                .grade(reviewBoard.getGrade())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .roomName(reviewBoard.getReservation().getRoom().getRoomName())
                .replies(replyDTOs)
                .regDate(reviewBoard.getRegTime())
                .modDate(reviewBoard.getModTime())
                .build();
    }
}
