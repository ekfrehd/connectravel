package org.ezone.room.repository;

import java.util.List;
import org.ezone.room.entity.ReviewBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewBoardRepository extends JpaRepository<ReviewBoard, Long> {

    @Query("SELECT rb FROM ReviewBoard rb JOIN rb.reservation r WHERE r.room.accommodation.ano = :ano")
    Page<ReviewBoard> findByAccommodationAno(@Param("ano") Long ano, Pageable pageable);

    @Query("SELECT rb, m, rv FROM ReviewBoard rb LEFT JOIN rb.member m LEFT JOIN rb.reservation rv where rb.rbno =:rbno ")
    Object getReviewBoardByRbno(@Param("rbno") Long rbno);
//
//    List<ReviewBoard> findByRoom_Rno(Long rno);
//
//    @Query("SELECT COUNT(rb) > 0 FROM ReviewBoard rb WHERE rb.reservation.rvno = :rvno")
//    boolean existsByReviewBoard(@Param("rvno") Long rvno);
//
//    void deleteByRoom_Rno(Long rno);
}
