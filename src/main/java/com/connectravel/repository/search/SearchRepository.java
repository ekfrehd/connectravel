package com.connectravel.repository.search;

import com.connectravel.entity.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface SearchRepository {

    /* 숙소 필터링 기능 */
    List<Accommodation> findByRegion(String Region);
    List<Accommodation> findByOptions(Set<Long> optionIds);

    List<Accommodation> findByRoomCriteria(int price, int minimumOccupancy, int maximumOccupancy, boolean operating);


}
