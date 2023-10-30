package org.ezone.room.repository;

import java.time.LocalDate;
import java.util.List;
import org.ezone.room.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long>, QuerydslPredicateExecutor<Accommodation>/*, SearchBoardRepository*/ {

    @Query("select a from Accommodation a where a.member.id = :id")
    Accommodation findAccommodationByMemberId(@Param("id") String id);

    @Query("select case when count(a)> 0 then true else false end from Accommodation a where a.member.id = :id")
    boolean existsAccommodationByMemberId(@Param("id") String id);

    @Query("select a from Accommodation a where a.member.email = :email")
    Accommodation findByEmail(@Param("email") String email);

    @Query("SELECT a ,i FROM Accommodation a left join AccommodationImg i on a = i.accommodation group by a.ano")
    List<Object[]> findAccommodationWithImages();

    // Join을 이용해 Room의 rno을 이용해 accommodation을 조회
    @Query("select r.accommodation FROM Room r where r.rno = :rno")
    Accommodation findAnoByRno(@Param("rno") Long rno);

    Accommodation findByAno(Long ano);


    //숙소 정보와 숙소가 가지고있는 방중 가장 작은 가격의 방을 보여줌. 날짜임력을통해 예약할수있는 방을 보여준다.
    //방중 하나라도 널값이면 널값으로 출력해야함.(빈방)
    //인덱스 0번 숙소,1번 가격 2번 예약정보(null일때 빈방임)
    @Query("select a, min(r.price) as min_price,rv.rvno as rvno" +
            " from Accommodation a join Room as r on a = r.accommodation" +
            " left join Reservation as rv on r = rv.room and rv.startDate >= :StartDate" +
            " and rv.endDate <= :EndDate where rvno is null group by a.ano")
    List<Object[]> findByRv_LowPrice(@Param("StartDate") LocalDate StartDate, @Param("EndDate")LocalDate EndDate);
}
