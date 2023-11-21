package com.connectravel.repository;

import com.connectravel.domain.entity.Accommodation;
import com.connectravel.repository.search.SearchBoardRepository;
import com.connectravel.repository.search.SearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, SearchRepository, SearchBoardRepository {

    Accommodation findByAno(Long ano);

    // 이메일을 사용하여 숙박업소 조회
    Optional<Accommodation> findBySellerEmail(String sellerEmail);

    // Member ID를 사용하여 숙박업소 조회
    Optional<Accommodation> findByMemberId(Long memberId);

    // Member ID를 사용하여 해당 Member가 Accommodation을 등록했는지 확인하는 메서드
    boolean existsByMemberId(Long memberId);


}