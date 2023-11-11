package com.connectravel.repository;

import com.connectravel.entity.TourBoard;
import com.connectravel.repository.search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface TourBoardRepository extends JpaRepository<TourBoard, Long>, QuerydslPredicateExecutor<TourBoard>, SearchBoardRepository {

    // 가이드와 가이드 리뷰 글을 결합하고 TourBoard의 정보와 TourBoardReview의 테이블 행 개수를 체크
    // tourBoard의 tbno와 주어진 tbno가 일치하는지 확인
    @Query("select b, count(r) from TourBoard b " +
            "left outer join TourBoardReview r on r.tourBoard = b " +
            "where b.tbno = :tbno")
    Object getBoardByTbno(@Param("tbno") Long tbno);
}
