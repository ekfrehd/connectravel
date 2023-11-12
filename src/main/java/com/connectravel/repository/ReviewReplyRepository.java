package com.connectravel.repository;

import com.connectravel.entity.ReviewBoard;
import com.connectravel.entity.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {

    @Modifying
    @Query("DELETE FROM ReviewReply rr WHERE rr.reviewBoard.rbno = :rbno")
    void deleteByRbno(@Param("rbno") Long rbno);

    //게시물로 댓글 목록 가져오기
    List<ReviewReply> getRepliesByReviewBoardOrderByRrno(ReviewBoard reviewBoard);

}
