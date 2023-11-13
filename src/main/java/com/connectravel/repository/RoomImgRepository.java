package com.connectravel.repository;

import com.connectravel.domain.entity.RoomImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomImgRepository extends JpaRepository<RoomImg, Long> {

    List<RoomImg> findByRoomRno(Long rno);

}