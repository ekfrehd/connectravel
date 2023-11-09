package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.ReviewBoardDTO;
import com.connectravel.entity.ReviewBoard;

import java.util.List;

public interface ReviewBoardService {

    Long createReview(ReviewBoardDTO dto);

    ReviewBoardDTO getReviewByBno(Long bno);

    void updateReview(ReviewBoardDTO reviewBoardDTO);

    void deleteReview(Long bno);

    PageResultDTO<ReviewBoardDTO, ReviewBoard> getPaginatedReviewsByAccommodation(Long ano, PageRequestDTO pageRequestDTO); // 리스트 출력

    List<ImgDTO> listReviewImages(Long rbno);
    

}
