package com.connectravel.service;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.dto.PageRequestDTO;
import com.connectravel.domain.dto.PageResultDTO;

import java.time.LocalDate;

public interface SearchService {

    PageResultDTO<AccommodationDTO, Object[]> searchAccommodationList(
            PageRequestDTO pageRequestDTO, String keyword, String category, String region,
            LocalDate startDate, LocalDate endDate, Integer inputedMinPrice, Integer inputedMaxPrice);

}