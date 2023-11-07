package com.connectravel.service;

import com.connectravel.dto.ReservationDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.Room;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ReservationRepository;
import com.connectravel.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReservationServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceTest.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Room savedRoom;
    private Member savedMember;

    @BeforeEach
    public void setUp() {
        // 더미 Room과 Member를 생성하고 저장하는 메소드가 있다고 가정
        savedMember = createAndSaveDummyMember();
        savedRoom = createAndSaveDummyRoom();
    }

    @Test
    @Transactional
    public void testBookRoom() {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setRno(savedRoom.getRno());
        reservationDTO.setMemberEmail(savedMember.getEmail());
        reservationDTO.setStartDate(LocalDate.now());
        reservationDTO.setEndDate(LocalDate.now().plusDays(2));
        reservationDTO.setGuestCount(2);

        // 방 예약
        ReservationDTO bookedReservation = reservationService.bookRoom(reservationDTO);

        // 예약 확인
        assertNotNull(bookedReservation, "예약은 null이 아니어야 합니다");
        assertEquals(savedRoom.getRno(), bookedReservation.getRno(), "방 ID가 일치해야 합니다");

        log.debug("예약된 정보: {}", bookedReservation);
    }

    /*@Test
    @Transactional
    public void testGetRoomBookingDetails() {
        // 먼저 방을 예약함
        ReservationDTO newReservation = bookDummyReservation();

        // 예약 정보 검색
        ReservationDTO bookingDetails = reservationService.getRoomBookingDetails(newReservation.getRvno());

        // 정보 확인
        assertNotNull(bookingDetails, "예약 정보는 null이 아니어야 합니다");
        assertEquals(newReservation.getRvno(), bookingDetails.getRvno(), "예약 ID가 일치해야 합니다");

        log.debug("예약 상세 정보: {}", bookingDetails);
    }

    @Test
    @Transactional
    public void testModifyRoomBooking() {
        // 먼저 방을 예약함
        ReservationDTO existingReservation = bookDummyReservation();

        // 예약 변경
        existingReservation.setGuestCount(3);
        ReservationDTO modifiedReservation = reservationService.modifyRoomBooking(existingReservation.getRvno(), existingReservation);

        // 변경 사항 확인
        assertNotNull(modifiedReservation);
        assertEquals(3, modifiedReservation.getGuestCount());

        log.debug("수정된 예약: {}", modifiedReservation);
    }

    @Test
    @Transactional
    public void testCancelRoomBooking() {
        // 먼저 방을 예약함
        ReservationDTO reservationToCancel = bookDummyReservation();

        // 예약 취소
        reservationService.cancelRoomBooking(reservationToCancel.getRvno());

        // 취소 확인을 위해 취소된 예약 검색 시도
        assertThrows(EntityNotFoundException.class, () -> reservationService.getRoomBookingDetails(reservationToCancel.getRvno()));
    }

    @Test
    @Transactional
    public void testListUserRoomBookings() {
        // 동일 사용자에 대해 여러 예약을 진행하는 메소드가 있다고 가정
        bookMultipleReservationsForUser(savedMember.getEmail());

        // 사용자의 예약 목록 조회
        List<ReservationDTO> bookings = reservationService.listUserRoomBookings(savedMember.getEmail());

        // 예약 목록 확인
        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());

        log.debug("사용자의 방 예약 목록: {}", bookings);
    }

    @Test
    @Transactional
    public void testListAccommodationRoomBookings() {
        // 동일 숙박 시설에 대해 여러 예약을 진행하는 메소드가 있다고 가정
        bookMultipleReservationsForAccommodation(savedRoom.getAccommodation().getAno());

        // 숙박 시설의 예약 목록 조회
        List<ReservationDTO> bookings = reservationService.listAccommodationRoomBookings(savedRoom.getAccommodation().getAno());

        // 예약 목록 확인
        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());

        log.debug("숙박 시설의 방 예약 목록: {}", bookings);
    }

    @Test
    @Transactional
    public void testListDateRangeRoomBookings() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(10);

        // 주어진 날짜 범위 내에 여러 예약을 진행하는 메소드가 있다고 가정
        bookReservationsForDateRange(startDate, endDate);

        // 날짜 범위 내의 예약 목록 조회
        List<ReservationDTO> bookings = reservationService.listDateRangeRoomBookings(startDate, endDate);

        // 예약 목록 확인
        assertNotNull(bookings);
        assertFalse(bookings.isEmpty());

        log.debug("날짜 범위 내 방 예약 목록: {}", bookings);
    }*/

    // 아래는 존재한다고 가정하는 헬퍼 메소드들입니다:
    // createAndSaveDummyMember(), createAndSaveDummyRoom(), bookDummyReservation(),
    // bookMultipleReservationsForUser(String email), bookMultipleReservationsForAccommodation(Long ano),
    // and bookReservationsForDateRange(LocalDate start, LocalDate end).
}
