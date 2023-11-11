package com.connectravel.repository.search;

import com.connectravel.entity.Accommodation;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface SearchRepository {
    List<Accommodation> findByRegion(String Region);
    List<Accommodation> findByOptions(Set<Long> optionIds);

    List<Accommodation> findByRoomCriteria(int price, int minimumOccupancy, int maximumOccupancy, boolean operating);

}
