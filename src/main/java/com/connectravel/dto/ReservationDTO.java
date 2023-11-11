package com.connectravel.dto;

import com.connectravel.constant.ReservationStatus;
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

    private Long rvno; // 예약 번호
    private String message; // 요청 사항
    private int money; // 결제 금액
    private int numberOfGuests; // 예약 인원 수
    private LocalDate startDate; // 예약 시작 일자
    private LocalDate endDate; // 예약 종료 일자
    private ReservationStatus status; // 예약 상태를 나타내는 열거형

    private RoomDTO roomDTO; // 예약한 방에 대한 정보
    private MemberDTO memberDTO; // 예약한 회원에 대한 정보

}
