package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.TourBoardReviewDTO;
import com.connectravel.entity.TourBoardReview;

import java.util.List;

public interface TourBoardReviewService {

    Long createTourBoardReview(TourBoardReviewDTO dto, String email);

    TourBoardReviewDTO getTourBoardReview(Long bno);

    void updateTourBoardReview(TourBoardReviewDTO tourBoardReivewDTO);

    void deleteTourBoardReviewWithReplies(Long tbrno, Long tbno);

    PageResultDTO<TourBoardReviewDTO, TourBoardReview> getPaginatedTourBoardReviews(Long trbno, PageRequestDTO pageRequestDTO); // 리스트 출력

    List<ImgDTO> listTourBoardReviewImages(Long tbrno);


}
