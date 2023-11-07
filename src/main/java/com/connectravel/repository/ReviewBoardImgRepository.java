package com.connectravel.repository;

import com.connectravel.entity.ReviewBoard;
import com.connectravel.entity.ReviewBoardImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewBoardImgRepository extends JpaRepository<ReviewBoardImg,Long> {

    @Query("SELECT i from ReviewBoardImg i where i.reviewBoard = :rbno")
    List<ReviewBoardImg> getImgByRbno(@Param("rbno") ReviewBoard rbno);

    @Modifying
    @Query("delete from ReviewBoardImg r where r.reviewBoard.rbno =:rbno")
    void deleteImgByRbno(Long rbno);

}
