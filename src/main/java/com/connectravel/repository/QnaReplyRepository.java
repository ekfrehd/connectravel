package com.connectravel.repository;

import com.connectravel.entity.QnaBoard;
import com.connectravel.entity.QnaReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaReplyRepository extends JpaRepository<QnaReply, Long> {

    List<QnaReply> getRepliesByQnaBoardOrderByQrno(QnaBoard qnaBoard);
}
