package com.connectravel.service;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.dto.PageRequestDTO;
import com.connectravel.domain.dto.PageResultDTO;

import java.time.LocalDate;
import java.util.Set;

public interface SearchService {

    PageResultDTO<AccommodationDTO, Object[]> searchAccommodationList(
            PageRequestDTO pageRequestDTO, String keyword, String accommodationType, String region,
            LocalDate startDate, LocalDate endDate, Integer inputedMinPrice, Integer inputedMaxPrice, Set<Long> optionIds);
}