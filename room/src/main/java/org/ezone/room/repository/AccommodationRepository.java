package org.ezone.room.repository;

import org.ezone.room.entity.AccommodationEntity;
import org.ezone.room.entity.AccommodationImgEntity;
import org.ezone.room.repository.search.SearchBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Long>, QuerydslPredicateExecutor<AccommodationEntity>, SearchBoardRepository {

    @Query("select a from Accomodation a where a.member.id = :id")
    AccommodationEntity findAccommodationByMemberId(@Param("id") String id);

    @Query("select case when count(a)> 0 then true else false end from Accomodation a where a.member.id = :id")
    boolean existsAccommodationByMemberId(@Param("id") String id);

    @Query("select a from Accomodation a where a.member.email = :email")
    AccommodationEntity findByEmail(@Param("email") String email);

    @Query("SELECT a ,i FROM Accomodation a left join AccommodationImgEntity i on a = i.accommodationEntity group by a.ano")
    List<Object[]> findAccommodationWithImages();

    // Join을 이용해 Room의 rno을 이용해 accommodation을 조회
    @Query("select r.accommodationEntity FROM RoomEntity r where r.rno = :rno")
    AccommodationEntity findAnoByRno(@Param("rno") Long rno);

    AccommodationEntity findByAno(Long ano);

    //숙소 정보와 숙소가 가지고있는 방중 가장 작은 가격의 방을 보여줌. 날짜임력을통해 예약할수있는 방을 보여준다.
    //방중 하나라도 널값이면 널값으로 출력해야함.(빈방)
    //인덱스 0번 숙소,1번 가격 2번 예약정보(null일때 빈방임)
    @Query("select a, min(r.price) as min_price,rv.rvno as rvno" +
            " from Accomodation a join RoomEntity as r on a = r.accommodationEntity" +
            " left join ReservationEntity as rv on r = rv.room_id and rv.StartDate >= :StartDate" +
            " and rv.EndDate <= :EndDate where rvno is null group by a.ano")
    List<Object[]> findByRv_LowPrice(@Param("StartDate") LocalDate StartDate, @Param("EndDate")LocalDate EndDate);
}
