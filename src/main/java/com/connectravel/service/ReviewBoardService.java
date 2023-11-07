package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.ReviewBoardDTO;
import com.connectravel.entity.Accommodation;
import com.connectravel.entity.ReviewBoard;
import javassist.NotFoundException;

import java.util.List;

public interface ReviewBoardService {

    Long register(ReviewBoardDTO dto) throws NotFoundException; //등록

    ReviewBoardDTO get(Long bno); //조회

    void modify(ReviewBoardDTO reviewBoardDTO); //수정

    void remove(Long bno); //삭제

    PageResultDTO<ReviewBoardDTO, ReviewBoard> getReviewBoardsAndPageInfoByAccommodationId(Long ano, PageRequestDTO pageRequestDTO); // 리스트 출력

    Accommodation findAccommodationByRoomId(Long rno);

    List<ImgDTO> getImgList(Long rbno);
    

}
