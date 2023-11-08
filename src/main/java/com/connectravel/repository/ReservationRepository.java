package com.connectravel.repository;

import com.connectravel.entity.Member;
import com.connectravel.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByMember(Member member);
    List<Reservation> findByMemberEmail(String email);
    List<Reservation> findByRoomAccommodationAno(Long ano);
    List<Reservation> findByRoomRno(Long rno);

    @Query("select r from Reservation r where r.room.rno = :rno and r.startDate <= :endDate and r.endDate >= :startDate")
    List<Reservation> findAvailableReservations(@Param("rno") Long rno, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
