package org.ezone.room.repository;

import org.ezone.room.entity.ReviewBoard;
import org.ezone.room.entity.TourBoard;
import org.ezone.room.entity.TourBoardReview;
import org.ezone.room.repository.search.SearchBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;

public interface TourRepository extends JpaRepository<TourBoard, Long>,
        QuerydslPredicateExecutor<TourBoard>, SearchBoardRepository {

}
