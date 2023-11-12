package com.connectravel.repository;

import com.connectravel.entity.TourBoardReview;
import com.connectravel.entity.TourBoardReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourBoardReviewImgRepository extends JpaRepository<TourBoardReviewImg, Long> {

    @Query("SELECT t from TourBoardReviewImg t where t.tourBoardReview = :tbrno")
    List<TourBoardReviewImg> getImgByTourBoardReviewTbrno(@Param("tbrno") TourBoardReview tbrno);
}
