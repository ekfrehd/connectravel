package com.connectravel.repository;

import com.connectravel.domain.entity.ReviewBoardImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewBoardImgRepository extends JpaRepository<ReviewBoardImg,Long> {

    @Query("SELECT i from ReviewBoardImg i where i.reviewBoard.rbno = :rbno")
    List<ReviewBoardImg> getImgByRbno(@Param("rbno") Long rbno);

    @Modifying
    @Query("DELETE FROM ReviewBoardImg rbi WHERE rbi.reviewBoard.rbno = :rbno")
    void deleteImgByRbno(@Param("rbno") Long rbno);

}