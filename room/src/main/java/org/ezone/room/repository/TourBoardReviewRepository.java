package org.ezone.room.repository;

import org.ezone.room.entity.TourBoardReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourBoardReviewRepository extends JpaRepository<TourBoardReview, Long> {

    Page<TourBoardReview> findByTourBoard_Tbno(Long tbno, Pageable pageable);

}
