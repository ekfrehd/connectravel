package com.connectravel.service;


import com.connectravel.domain.dto.ReservationDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    /* 예약 생성, 조회, 수정, 취소(요청,승인) */
    ReservationDTO registerReservation(ReservationDTO reservationDTO);

    boolean requestCancel(Long rvno, String userEmail);

    boolean approveCancellation(Long rvno);

    /* 예약 목록 조회 */
    List<ReservationDTO> listUserRoomBookings(String userEmail);

    List<ReservationDTO> listRoomBookings(Long memberId);


    /* 예약 가능 여부를 확인용 */
    boolean checkRoomAvailability(Long rno, LocalDate startDate, LocalDate endDate);

}