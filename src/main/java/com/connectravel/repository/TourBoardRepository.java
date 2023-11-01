package com.connectravel.repository;

import com.connectravel.entity.TourBoard;
import com.connectravel.repository.search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TourBoardRepository extends JpaRepository<TourBoard, Long>, QuerydslPredicateExecutor<TourBoard>, SearchBoardRepository {

    /*@Query("select b, count(r) from TourBoard b left outer join TourBoardReview r on r.tourBoard = b where b.tbno = :tbno")
    Object getBoardByBno(@Param("tbno") Long tbno);*/
}
