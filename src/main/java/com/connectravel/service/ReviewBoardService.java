package com.connectravel.service;

import com.connectravel.domain.dto.ImgDTO;
import com.connectravel.domain.dto.PageRequestDTO;
import com.connectravel.domain.dto.PageResultDTO;
import com.connectravel.domain.dto.ReviewBoardDTO;
import com.connectravel.domain.entity.ReviewBoard;

import java.util.List;

public interface ReviewBoardService {

    Long createReview(ReviewBoardDTO dto);

    ReviewBoardDTO getReviewByRbno(Long rbno);

    void updateReview(ReviewBoardDTO reviewBoardDTO);

    void deleteReview(Long bno);

    PageResultDTO<ReviewBoardDTO, ReviewBoard> getPaginatedReviewsByAccommodation(Long ano, PageRequestDTO pageRequestDTO); // 리스트 출력

    List<ImgDTO> listReviewImages(Long rbno);

}