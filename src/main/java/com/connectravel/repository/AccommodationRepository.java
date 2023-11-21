package com.connectravel.repository;

import com.connectravel.domain.entity.Accommodation;
import com.connectravel.repository.search.SearchBoardRepository;
import com.connectravel.repository.search.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, SearchRepository, SearchBoardRepository {

    Accommodation findByAno(Long ano);

    // 이메일을 사용하여 숙박업소 조회
    Optional<Accommodation> findBySellerEmail(String sellerEmail);

    // Member ID를 사용하여 숙박업소 조회
    Optional<Accommodation> findByMemberId(Long memberId);

    // Member ID를 사용하여 해당 Member가 Accommodation을 등록했는지 확인하는 메서드
    boolean existsByMemberId(Long memberId);

    // 검색 필터링 메서드
    Page<Accommodation> searchFilteredAccommodations(String region, Set<Long> optionIds,
                                                     LocalDate startDate, LocalDate endDate,
                                                     Integer minPrice, Integer maxPrice, Pageable pageable);
}