package com.connectravel.repository;

import com.connectravel.entity.TourBoard;
import com.connectravel.entity.TourBoardImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourBaordImgRepository extends JpaRepository<TourBoardImg, Long> {

    @Query("SELECT i from TourBoardImg i where i.tourBoard = :tbno")
    List<TourBoardImg> GetImgbyTourBoardId(@Param("tbno") TourBoard tbno);
}
