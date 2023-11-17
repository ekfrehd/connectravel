package com.connectravel.repository;

import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByMember(Member member);

    Reservation findByRvno(@Param("rvno")Long Rvno);

    List<Reservation> findByMember_Username(String username);

    List<Reservation> findByRoomAccommodationAno(Long ano);

    List<Reservation> findByRoomRno(Long rno);

    @Query("select r from Reservation r where r.room.rno = :rno and r.startDate <= :endDate and r.endDate >= :startDate")
    List<Reservation> findAvailableReservations(@Param("rno") Long rno, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}