package com.connectravel.repository.search;

import com.connectravel.domain.entity.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Set;

public interface SearchRepository {

    /* 숙소 필터링 기능 */
    Page<Accommodation> searchFilteredAccommodations(String region, Set<Long> optionIds,
                                                       LocalDate startDate, LocalDate endDate,
                                                       Integer minPrice, Integer maxPrice, Pageable pageable) ;

}