package org.ezone.room.repository;

import java.util.List;
import org.ezone.room.entity.ReviewBoard;
import org.ezone.room.entity.ReviewBoardImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewBoardImgRepository extends JpaRepository<ReviewBoardImg,Long> {

    @Query("SELECT i from ReviewBoardImg i where i.reviewBoard = :rbno")
    List<ReviewBoardImg> GetImgbyrbno(@Param("rbno") ReviewBoard rbno);

}
