package org.ezone.room.repository;

import org.ezone.room.entity.Member;
import org.ezone.room.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<ReservationEntity,Long> {
    @Query("select r from ReservationEntity r where r.member_id = :member_id")
    List<ReservationEntity> findByMember(@Param("member_id")Member member_id);
    @Query("select r from ReservationEntity r where r.rvno = :rvno and r.StartDate > :Today " +
            " and r.member_id = :member")
    Optional<ReservationEntity> findByRvnoAndDateAndMember_id
            (@Param("Today") LocalDate Today, @Param("rvno")Long rvno,@Param("member") Member member);


    public ReservationEntity findByRvno(@Param("rvno")Long Rvno);
}

