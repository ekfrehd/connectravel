package com.connectravel.repository;

import com.connectravel.entity.QnaBoard;
import com.connectravel.repository.search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface QnaBoardRepository extends JpaRepository<QnaBoard, Long>, QuerydslPredicateExecutor<QnaBoard>, SearchBoardRepository {

    @Query("SELECT b, w, COUNT(r) " +
            "FROM QnaBoard b " +
            "LEFT JOIN b.member w " +
            "LEFT OUTER JOIN QnaReply r ON r.qnaBoard = b " +
            "WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

}
