package com.connectravel.repository;

import com.connectravel.entity.TourBoard;
import com.connectravel.repository.search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TourBoardRepository extends JpaRepository<TourBoard, Long>, QuerydslPredicateExecutor<TourBoard>, SearchBoardRepository {

}
