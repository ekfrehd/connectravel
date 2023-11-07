package com.connectravel.repository;

import com.connectravel.entity.ReviewBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewBoardRepository extends JpaRepository<ReviewBoard, Long> {

    @Query("SELECT rb FROM ReviewBoard rb JOIN rb.reservation r WHERE r.room.accommodation.ano = :ano")
    Page<ReviewBoard> findByAccommodationAno(@Param("ano") Long ano, Pageable pageable);
    // 숙소의 아이디(ano)를 기준으로 모든 리뷰 게시글들을 페이지 형태로 조회

}
