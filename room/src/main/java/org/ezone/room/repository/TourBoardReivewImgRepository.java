package org.ezone.room.repository;

import org.ezone.room.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourBoardReivewImgRepository extends JpaRepository<TourBoardReviewImg, Long> {
    @Query("SELECT t from TourBoardReviewImg t where t.tourBoardReview = :tbrno")
    List<TourBoardReviewImg> GetImgbyTourBoardReviewId(@Param("tbrno") TourBoardReview tbrno);
}
