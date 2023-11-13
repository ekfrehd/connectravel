package com.connectravel.repository;

import com.connectravel.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomName(String roomName);

    List<Room> findByAccommodationAno(Long ano);

    @Query("SELECT r, i, rv FROM Room r LEFT JOIN r.images i LEFT JOIN r.reservations rv WHERE r.accommodation.ano = :accommodationId AND rv.startDate >= :startDate AND rv.endDate <= :endDate")
    List<Object[]> findRoomsByAccommodationAndDate(Long accommodationId, LocalDate startDate, LocalDate endDate);
}
