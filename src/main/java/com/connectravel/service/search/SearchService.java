package com.connectravel.service.search;

import com.connectravel.dto.search.AccommodationSearchDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface SearchService {
    List<AccommodationSearchDTO> searchByRegion(String region);
    List<AccommodationSearchDTO> searchByOptions(String region, Set<Long> optionIds);
    List<AccommodationSearchDTO> searchByRoomCriteria(String region, int price, int minimumOccupancy, int maximumOccupancy, boolean operating);
    List<AccommodationSearchDTO> searchByRegionOptionsAndRoomCriteria(String region, Set<Long> optionIds, int price, int minimumOccupancy, int maximumOccupancy, boolean operating);
}
