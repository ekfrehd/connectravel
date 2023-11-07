package com.connectravel.repository;

import com.connectravel.entity.ReviewBoard;
import com.connectravel.entity.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Long> {

    // 해당되는 게시물의 번호의 댓글을 삭제한다.
    @Modifying
    @Query("delete from ReviewReply r where r.reviewBoard.rbno =:rbno")
    void deleteByRbno(Long rbno);

    //게시물로 댓글 목록 가져오기
    List<ReviewReply> getRepliesByReviewBoardOrderByRrno(ReviewBoard reviewBoard);

}
