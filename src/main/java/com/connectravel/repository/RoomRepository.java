package com.connectravel.repository;

import com.connectravel.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomName(String roomName);

    // 특정 숙소에 속한 방들을 찾는 메서드
    List<Room> findByAccommodationAno(Long ano);

}