package com.connectravel.repository;

import com.connectravel.domain.entity.Accommodation;
import com.connectravel.repository.search.SearchBoardRepository;
import com.connectravel.repository.search.SearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, SearchRepository, SearchBoardRepository {

    Optional<Accommodation> findBySellerEmail(String sellerEmail);

    // Member ID를 사용하여 해당 Member가 Accommodation을 등록했는지 확인하는 메서드
    boolean existsByMemberId(Long memberId);

}