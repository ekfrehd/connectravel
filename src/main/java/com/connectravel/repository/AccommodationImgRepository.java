package com.connectravel.repository;

import com.connectravel.entity.AccommodationImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationImgRepository extends JpaRepository<AccommodationImg, Long> {
    List<AccommodationImg> findByAccommodationAno(Long ano);
}
