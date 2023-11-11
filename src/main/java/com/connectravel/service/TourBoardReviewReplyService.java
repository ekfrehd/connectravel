package com.connectravel.service;

import com.connectravel.dto.TourBoardReviewReplyDTO;

public interface TourBoardReviewReplyService {

    Long createTourBoardReviewReply(TourBoardReviewReplyDTO TourBoardReviewReplyDTO);

    TourBoardReviewReplyDTO getTourBoardReviewReply(Long tbrrno);

    void updateTourBoardReviewReply(TourBoardReviewReplyDTO TourBoardReviewReplyDTO);

    void deleteTourBoardReviewReply(Long tbrrno);

}
