package com.connectravel.repository;

import com.connectravel.entity.Accommodation;
import com.connectravel.repository.search.SearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, SearchRepository {
    Optional<Accommodation> findBySellerEmail(String sellerEmail);
}
