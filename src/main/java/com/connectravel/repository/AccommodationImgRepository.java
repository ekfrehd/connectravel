package com.connectravel.repository;

import com.connectravel.entity.Accommodation;
import com.connectravel.entity.AccommodationImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccommodationImgRepository extends JpaRepository<AccommodationImg, Long> {

    @Query("SELECT i from AccommodationImg i where i.accommodation = :ano")
    List<AccommodationImg> GetImgbyAccommodationId(@Param("ano") Accommodation ano);
}
