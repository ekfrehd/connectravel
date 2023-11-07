package com.connectravel.dto;

import com.connectravel.entity.Member;
import com.connectravel.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long rvno;

    private String message; //요청사항

    private int numberOfGuests; // 예약 인원 수

    private LocalDate startDate; //예약 시작
    private LocalDate endDate; //예약 종료
    private int money;

    private Room room;

    private Member member;

    private boolean state = true;

}
