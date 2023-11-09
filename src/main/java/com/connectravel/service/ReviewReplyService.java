package com.connectravel.service;

import com.connectravel.dto.ReviewReplyDTO;

import java.util.List;

public interface ReviewReplyService {

    Long createReviewReply(ReviewReplyDTO reviewReplyDTO);
    List<ReviewReplyDTO> getRepliesByReviewRbno(Long rbno);
    void updateReviewReply(ReviewReplyDTO reviewReplyDTO);
    void deleteReviewReply(Long rrno);

}
