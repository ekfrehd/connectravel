package com.connectravel.service;


import com.connectravel.dto.ReservationDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    ReservationDTO bookRoom(ReservationDTO reservationDTO);
    // 예약 생성
    ReservationDTO getRoomBookingDetails(Long rvno);
    // 예약 조회
    ReservationDTO modifyRoomBooking(Long rvno, ReservationDTO reservationDTO);
    // 예약 수정
    void cancelRoomBooking(Long rvno);
    // 예약 취소
    List<ReservationDTO> listUserRoomBookings(String userEmail);
    // 사용자별 예약 목록 조회. 여기서 사용자 ID 대신 이메일 사용.
    List<ReservationDTO> listAccommodationRoomBookings(Long ano);
    // 숙소별 예약 목록 조회
    List<ReservationDTO> listRoomBookings(Long rno);
    // 방별 예약 목록 조회
    boolean checkRoomAvailability(Long rno, LocalDate startDate, LocalDate endDate);
    // 예약 가능 여부 확인

    // void updateRoomBookingStatus(Long rvno, ReservationStatus status); // 예약 상태 변경
}

