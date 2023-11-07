package com.connectravel.service;

import com.connectravel.dto.ReservationDTO;
import com.connectravel.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public ReservationDTO bookRoom(ReservationDTO reservationDTO) {
        // 예약 생성 로직 구현
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationDTO getRoomBookingDetails(Long rvno) {
        // 예약 조회 로직 구현
        return null;
    }

    @Override
    @Transactional
    public ReservationDTO modifyRoomBooking(Long rvno, ReservationDTO reservationDTO) {
        // 예약 수정 로직 구현
        return null;
    }

    @Override
    @Transactional
    public void cancelRoomBooking(Long rvno) {
        // 예약 취소 로직 구현
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> listUserRoomBookings(String userEmail) {
        // 사용자별 예약 목록 조회 로직 구현
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> listAccommodationRoomBookings(Long ano) {
        // 숙소별 예약 목록 조회 로직 구현
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> listRoomBookings(Long rno) {
        // 방별 예약 목록 조회 로직 구현
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkRoomAvailability(Long rno, LocalDate startDate, LocalDate endDate) {
        // 예약 가능 여부 확인 로직 구현
        return true;
    }

   /* @Override
    @Transactional
    public void updateRoomBookingStatus(Long rvno, ReservationStatus status) {
        // 예약 상태 변경 로직 구현
    }*/
}
