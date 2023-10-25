package org.ezone.room.service;

import javassist.NotFoundException;
import org.ezone.room.dto.*;
import org.ezone.room.entity.*;

import java.util.List;

public interface TourBoardReviewService {


    Long register(TourBoardReivewDTO dto) throws NotFoundException; //등록

    TourBoardReivewDTO get(Long bno); //조회

    void modify(TourBoardReivewDTO tourBoardReivewDTO); //수정

    public void removeWithReplies(Long tbrno, Long tbno) throws NotFoundException; //삭제

    public PageResultDTO<TourBoardReivewDTO, TourBoardReview> getTourReviewBoardsAndPageInfoByTourBoardId(Long trbno, PageRequestDTO pageRequestDTO); // 리스트 출력

    public List<ImgDTO> getImgList(Long tbrno);
    
    // DTO 객체를 Entity 객체로 변환하는 메소드
    default TourBoardReview dtoToEntity(TourBoardReivewDTO dto, TourBoard tourBoard, Member member) {

        return TourBoardReview.builder()
                .tbrno(dto.getTbrno())
                .content(dto.getContent())
                .grade(dto.getGrade())
                .member(member)
                .tourBoard(tourBoard)
                .build();
    }

    // Entity 객체를 DTO 객체로 변환하는 메소드
    default TourBoardReivewDTO entityToDTO(TourBoardReview tourBoardReview, Member member) {

        return TourBoardReivewDTO.builder()
                .tbrno(tourBoardReview.getTbrno())
                .content(tourBoardReview.getContent())
                .grade(tourBoardReview.getGrade())
                .writerEmail(member.getEmail())
                .writerName(member.getNickName())
                .regDate(tourBoardReview.getRegTime())
                .modDate(tourBoardReview.getModTime())
                .build();
    }
}
