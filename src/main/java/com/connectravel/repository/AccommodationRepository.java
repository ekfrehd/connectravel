package com.connectravel.repository;

import com.connectravel.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    Optional<Accommodation> findByEmail(String email);
}
