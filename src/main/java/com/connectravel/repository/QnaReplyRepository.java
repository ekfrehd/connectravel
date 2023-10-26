package com.connectravel.repository;

import com.connectravel.entity.QnaBoard;
import com.connectravel.entity.QnaReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaReplyRepository extends JpaRepository<QnaReply, Long> {

    /*// 해당되는 게시물의 번호의 댓글을 삭제한다.
    @Modifying
    @Query("delete from QnaReply r where r.qnaBoard.bno =:bno")
    void deleteByBno(Long bno);*/

    //게시물로 댓글 목록 가져오기
    List<QnaReply> getRepliesByQnaBoardOrderByRno(QnaBoard qnaBoard);
}
