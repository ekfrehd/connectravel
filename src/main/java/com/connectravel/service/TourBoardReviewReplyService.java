package com.connectravel.service;

import com.connectravel.dto.TourBoardReviewReplyDTO;

public interface TourBoardReviewReplyService {

    Long createTourBoardReviewReply(TourBoardReviewReplyDTO TourBoardReviewReplyDTO);

    void updateTourBoardReviewReply(TourBoardReviewReplyDTO TourBoardReviewReplyDTO);

    void deleteTourBoardReviewReply(Long rno);

}
