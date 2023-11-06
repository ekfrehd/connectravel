package org.ezone.room.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.ezone.room.entity.Accommodation;
import org.ezone.room.entity.Member;
import org.ezone.room.entity.Reservation;
import org.ezone.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("select r from Reservation r where r.member = :member_id")
    List<Reservation> findByMember(@Param("member_id")Member member_id);

    @Query("select r from Reservation r where r.rvno = :rvno and r.startDate > :Today " +
            " and r.member = :member")
    Optional<Reservation> findByRvnoAndDateAndMember_id
            (@Param("Today") LocalDate Today, @Param("rvno")Long rvno,@Param("member") Member member);


    public Reservation findByRvno(@Param("rvno")Long Rvno);

    Member findByMemberId(String id);

    Room findByRoomRno(Long rno);
}

