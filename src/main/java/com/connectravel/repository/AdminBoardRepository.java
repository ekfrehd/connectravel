package com.connectravel.repository;

import com.connectravel.entity.AdminBoard;
import com.connectravel.repository.search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface AdminBoardRepository extends JpaRepository<AdminBoard, Long>, QuerydslPredicateExecutor<AdminBoard>, SearchBoardRepository {

    @Query("select b, w, count(r) from AdminBoard b " +
            "left join b.member w " +
            "left outer join AdminReply r on r.adminBoard = b " +
            "where b.abno = :abno")
    Object getBoardByAbno(@Param("abno") Long abno);
    long countByCategory(String category);

}
