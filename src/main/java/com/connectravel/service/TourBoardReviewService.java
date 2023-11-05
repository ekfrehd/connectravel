package com.connectravel.service;

import com.connectravel.domain.dto.ImgDTO;
import com.connectravel.domain.dto.PageRequestDTO;
import com.connectravel.domain.dto.PageResultDTO;
import com.connectravel.domain.dto.TourBoardReviewDTO;
import com.connectravel.domain.entity.TourBoardReview;

import java.util.List;

public interface TourBoardReviewService {

    Long createTourBoardReview(TourBoardReviewDTO dto, String email);

    TourBoardReviewDTO getTourBoardReview(Long bno);

    void updateTourBoardReview(TourBoardReviewDTO tourBoardReivewDTO);

    void deleteTourBoardReviewWithReplies(Long tbrno);

    PageResultDTO<TourBoardReviewDTO, TourBoardReview> getPaginatedTourBoardReviews(Long trbno, PageRequestDTO pageRequestDTO); // 리스트 출력

    List<ImgDTO> listTourBoardReviewImages(Long tbrno);

}
