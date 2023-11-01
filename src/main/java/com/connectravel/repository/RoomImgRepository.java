package com.connectravel.repository;

import com.connectravel.entity.Room;
import com.connectravel.entity.RoomImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomImgRepository extends JpaRepository<RoomImg, Long> {

    @Query("SELECT i from RoomImg i where i.room = :room")
    List<RoomImg> GetImgbyRoomId(@Param("room") Room room);
}
