package com.connectravel.repository;

import com.connectravel.entity.TourBoardReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourBoardReviewRepository extends JpaRepository<TourBoardReview, Long> {

    Page<TourBoardReview> findByTourBoardTbno(Long tbno, Pageable pageable);
}
