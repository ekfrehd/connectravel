package org.ezone.room.repository;

import org.ezone.room.entity.ReviewBoard;
import org.ezone.room.entity.ReviewBoardImgEntity;
import org.ezone.room.entity.RoomEntity;
import org.ezone.room.entity.RoomImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewBoardImgRepository extends JpaRepository<ReviewBoardImgEntity,Long> {

    @Query("SELECT i from ReviewBoardImgEntity i where i.reviewBoard = :rbno")
    List<ReviewBoardImgEntity> GetImgbyrbno(@Param("rbno") ReviewBoard rbno);

}
