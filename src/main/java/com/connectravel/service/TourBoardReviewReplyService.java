package com.connectravel.service;

import com.connectravel.domain.dto.TourBoardReviewReplyDTO;

public interface TourBoardReviewReplyService {

    Long createTourBoardReviewReply(TourBoardReviewReplyDTO TourBoardReviewReplyDTO);

    TourBoardReviewReplyDTO getTourBoardReviewReply(Long tbrrno);

    void updateTourBoardReviewReply(TourBoardReviewReplyDTO TourBoardReviewReplyDTO);

    void deleteTourBoardReviewReply(Long tbrrno);

}