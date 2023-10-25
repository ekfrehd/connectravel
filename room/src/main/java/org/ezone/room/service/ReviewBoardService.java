package org.ezone.room.service;

import javassist.NotFoundException;
import org.ezone.room.dto.*;
import org.ezone.room.entity.*;
import org.ezone.room.repository.RoomRepository;

import java.util.List;
import java.util.stream.Collectors;

public interface ReviewBoardService {


    Long register(ReviewBoardDTO dto) throws NotFoundException; //등록

    ReviewBoardDTO get(Long bno); //조회

    void modify(ReviewBoardDTO reviewBoardDTO); //수정

    void removeWithReplies(Long bno); //삭제

    PageResultDTO<ReviewBoardDTO, ReviewBoard> getReviewBoardsAndPageInfoByAccommodationId(Long ano, PageRequestDTO pageRequestDTO); // 리스트 출력

    AccommodationEntity findAccommodationByRoomId(Long rno);

    public List<ImgDTO> getImgList(Long rbno);
    
    // DTO 객체를 Entity 객체로 변환하는 메소드
    default ReviewBoard dtoToEntity(ReviewBoardDTO dto, Member member, AccommodationEntity accommodation, RoomEntity room, ReservationEntity reservation) {

        return ReviewBoard.builder()
                .rbno(dto.getRbno())
                .content(dto.getContent())
                .grade(dto.getGrade())
                .member(member)
                .accommodation(accommodation)
                .room(room)
                .reservation(reservation)
                .build();
    }

    // Entity 객체를 DTO 객체로 변환하는 메소드
    default ReviewBoardDTO entityToDTO(ReviewBoard reviewBoard, Member member, List<ReviewReplyDTO> replyDTOs) {

        return ReviewBoardDTO.builder()
                .rbno(reviewBoard.getRbno())
                .content(reviewBoard.getContent())
                .grade(reviewBoard.getGrade())
                .writerEmail(member.getEmail())
                .writerName(member.getNickName())
                .roomName(reviewBoard.getRoom().getRoom_name())
                .replies(replyDTOs)
                .regDate(reviewBoard.getRegTime())
                .modDate(reviewBoard.getModTime())
                .build();
    }
}
