package org.ezone.room.repository;

import org.ezone.room.dto.RoomDTO;
import org.ezone.room.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity,Long> {

    @Query("SELECT r FROM RoomEntity r JOIN r.accommodationEntity a WHERE r.rno = :rno")
    RoomEntity findByRoom_nameByRno(@Param("rno") Long rno);

    @Query("SELECT r, i FROM RoomEntity r LEFT JOIN RoomImgEntity i ON r = i.room_id WHERE r.accommodationEntity.ano = :ano GROUP BY r.rno")
    List<Object[]> findRoomWithImages(@Param("ano") Long ano);

    //room+rv 검증할때 사용되는 쿼리문
    @Query("select room, r from RoomEntity room left join ReservationEntity as r " +
            "on room = r.room_id and r.StartDate >= :start_date and r.EndDate <= :end_date" +
            " where room.rno = :rno" +
            " group by room.rno")
    List<Object[]> findByDateByRno(@Param("start_date") LocalDate start_date, @Param("end_date") LocalDate end_date, @Param("rno") Long rno);

    //room+rv+img join (방 예약 리스트 정보 조회할때)
    //여기서,예약 정보까지 조인하는 이유는 방이 찼는지 안찼는지 확인해야하니까....(내가 작성해놓고 까먹음)
    @Query("select room, img.Imgfile, r.rvno from RoomEntity room left join ReservationEntity as r " +
            "on room = r.room_id and (r.StartDate < :end_date and r.EndDate > :start_date) and r.state = true " +
            "left join RoomImgEntity as img on room = img.room_id " +
            "where room.accommodationEntity.ano = :ano " +
            "group by room.rno")
    List<Object[]> findByDateandImgandAno(@Param("start_date") LocalDate start_date, @Param("end_date") LocalDate end_date, @Param("ano") Long ano);

    @Query("select room.room_name,count(r.room_id) * room.price from RoomEntity as room " +
            "left join ReservationEntity as r " +
            "on r.room_id = room and date(r.regTime) >= :StartDate and date(r.regTime) <= :EndDate and r.state = true" +
            " where room.accommodationEntity.ano = :ano group by room.rno ")
    List<Object[]> findSalesByDate(@Param("StartDate") Date StartDate, @Param("EndDate")Date EndDate, @Param("ano")Long ano);

    @Query("select rv.rvno, rv.state, rv.regTime,rv.StartDate,rv.EndDate, reserver, rv.message from Member m" +
            " join Accomodation a on m.id = a.member" +
            " join RoomEntity r on r.accommodationEntity = a" +
            " join ReservationEntity rv on rv.room_id = r" +
            " join Member reserver on reserver.id = rv.member_id" +
            " where m.email = :email")
    List<Object[]> SellersfindReservation(String email);
}
