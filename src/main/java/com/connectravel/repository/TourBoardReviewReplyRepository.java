package com.connectravel.repository;

import com.connectravel.domain.entity.TourBoardReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourBoardReviewReplyRepository extends JpaRepository<TourBoardReviewReply, Long> {


/*    // 해당되는 게시물의 번호의 댓글을 삭제한다.
    @Modifying
    @Query("delete from TourBoardReviewReply r where r.tourBoardReview.tbrno =:tbrno")
    void deleteByRbno(Long tbrno);*/

    // 게시물로 댓글 목록 가져오기
    // 대댓글 엔티티와 댓글 엔티티 사용, 댓글에 속한 대댓글들을 가져온다. rrno 기준으로 정렬하여 반환
    /*List<TourBoardReviewReply> getRepliesBytourBoardReviewOrderBytbrno(TourBoardReview tourBoardReview);*/

    /*List<TourBoardReviewReply> findBytourBoardReviewOrderBytbrno(TourBoardReview tourBoardReview);*/

}