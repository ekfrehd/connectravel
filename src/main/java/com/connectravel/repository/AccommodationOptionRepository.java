package com.connectravel.repository;

import com.connectravel.domain.entity.AccommodationOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationOptionRepository extends JpaRepository<AccommodationOption, Long> {

    List<AccommodationOption> findByAccommodationAno(Long ano);

}