package com.connectravel.repository;

import com.connectravel.entity.TourBoardReview;
import com.connectravel.entity.TourBoardReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourBoardReviewReplyRepository extends JpaRepository<TourBoardReviewReply, Long> {

    //게시물로 댓글 목록 가져오기
    // 대댓글 엔티티와 댓글 엔티티 사용, 댓글에 속한 대댓글들을 가져온다. rrno 기준으로 정렬하여 반환
    //List<TourBoardReviewReply> getRepliesByTourBoardReviewOrderByTbrno(TourBoardReview tourBoardReview);
    //List<TourBoardReviewReply> getTourBoardReviewRepliesByTourBoardReviewOrderByTbrrno(TourBoardReview tourBoardReview);
    List<TourBoardReviewReply> getRepliesByTourBoardReviewOrderByTbrrno(TourBoardReview tourBoardReview);
/*
    @Query("SELECT reply FROM TourBoardReviewReply reply JOIN reply.tourBoardReview review WHERE review.tbrno = :tbrno ORDER BY reply.tbrrno")
    List<TourBoardReviewReply> getRepliesByTourBoardReviewOrderByTbrrno(@Param("tbrno") Long tbrno);*/
}
