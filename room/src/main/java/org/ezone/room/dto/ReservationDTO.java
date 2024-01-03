package org.ezone.room.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO implements Comparable<ReservationDTO> {
    private Long rvno; //예약 번호
    private RoomDTO room_id; //방 번호
    private String message; //요청사항
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate StartDate; //예약 시작
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate EndDate; //예약 종료
//    private MemberFormDto member_id;
    private AccommodationDTO acc;

    private LocalDateTime regTime;

    private boolean state;

    private int money; // 돈

    @Override
    public int compareTo(ReservationDTO o) {
        return this.StartDate.compareTo(o.StartDate);
    }

    private boolean existReview; // 리뷰등록 여부
}