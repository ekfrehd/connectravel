package org.ezone.room.repository;

import org.ezone.room.entity.RoomEntity;
import org.ezone.room.entity.RoomImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomImgRepository extends JpaRepository<RoomImgEntity ,Long> {
    @Query("SELECT i from RoomImgEntity i where i.room_id = :room_id")
    List<RoomImgEntity> GetImgbyRoomId(@Param("room_id") RoomEntity room_id);
}
