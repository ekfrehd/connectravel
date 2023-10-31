package com.connectravel.repository;

import com.connectravel.entity.Member;
import com.connectravel.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByMember(Member member);
}
