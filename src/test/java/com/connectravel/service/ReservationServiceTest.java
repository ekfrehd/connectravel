package com.connectravel.service;

import com.connectravel.constant.ReservationStatus;
import com.connectravel.dto.AccommodationDTO;
import com.connectravel.dto.MemberDTO;
import com.connectravel.dto.ReservationDTO;
import com.connectravel.dto.RoomDTO;
import com.connectravel.entity.Accommodation;
import com.connectravel.entity.Member;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReservationServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceTest.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    private Member testMember;
    private RoomDTO testRoom;
    private ReservationDTO testReservation;
    private Accommodation testAccommodation;

    @BeforeEach
    public void setUp() {
        // 회원 정보 생성 및 저장
        testMember = Member.builder()
                .name("Test Member Name")
                .nickName("Test Member NickName")
                .email("testmember5@example.com")
                .build();
        memberRepository.save(testMember);

        testAccommodation = Accommodation.builder()
                .accommodationName("테스트 숙소")
                .sellerName(testMember.getName())
                .sellerEmail(testMember.getEmail())
                .postal(12345)
                .address("테스트 주소")
                .count(0)
                .region("테스트 지역")
                .tel("010-1234-5678")
                .accommodationType("모텔")
                .member(testMember)
                .build();
        accommodationRepository.save(testAccommodation);

        // 방 정보 생성 및 저장
        testRoom = roomService.registerRoom(RoomDTO.builder()
                .roomName("스위트룸")
                .price(150000)
                .accommodationDTO(AccommodationDTO.builder().ano(testAccommodation.getAno()).build())
                .build());

        // 예약 정보 생성
        testReservation = ReservationDTO.builder()
                .message("테스트 예약입니다")
                .money(150000)
                .numberOfGuests(2)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(2))
                .status(ReservationStatus.ACTIVE) // 상태를 ACTIVE로 설정
                .roomDTO(testRoom) // 예약할 방의 정보
                .memberDTO(MemberDTO.builder() // 예약자의 정보
                        .id(testMember.getId())
                        .name(testMember.getName())
                        .nickName(testMember.getNickName())
                        .email(testMember.getEmail())
                        .build())
                .build();
    }

    @Test
    // @Transactional
    void testBookRoom() {
        // 예약 생성 테스트
        ReservationDTO createdReservation = reservationService.bookRoom(testReservation);
        assertNotNull(createdReservation, "예약이 생성되어야 합니다.");

        log.debug("Created Reservation : {} ", createdReservation);
    }

    @Test
    @Transactional
    void testGetRoomBookingDetails() {
        // 테스트를 위한 예약을 미리 생성하고 그 rvno를 사용
        Long rvno = 1L; // 테스트용 예약 번호
        ReservationDTO reservation = reservationService.getRoomBookingDetails(rvno);

        assertNotNull(reservation, "예약 정보가 조회되어야 합니다.");
        assertEquals(rvno, reservation.getRvno(), "예약 번호가 일치해야 합니다.");
        log.debug("Get RoomBookingDetails : {}", reservation);
    }


    @Test
    @Transactional
    void testModifyExistingRoomBooking() {
        // 테스트를 위한 새 예약 생성하여 실제 존재하는지 확인
        ReservationDTO newReservation = reservationService.bookRoom(testReservation);
        Long reservationId = newReservation.getRvno(); // 생성된 예약 ID 사용

        // 예약 상세 정보 수정
        ReservationDTO modifyReservation = ReservationDTO.builder()
                .rvno(reservationId)
                .message("modify reservation")
                .money(150000)
                .numberOfGuests(4)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(2))
                .status(ReservationStatus.ACTIVE) // 'status' 필드를 정확하게 사용 ('state' 대신)
                .roomDTO(testRoom)
                .memberDTO(MemberDTO.builder()
                        .id(testMember.getId())
                        .name(testMember.getName())
                        .nickName(testMember.getNickName())
                        .email(testMember.getEmail())
                        .build())
                .build();

        // 기존 예약 수정 메소드 호출
        ReservationDTO updatedReservation = reservationService.modifyRoomBooking(reservationId, modifyReservation);

        // 업데이트가 성공적이었는지 확인하는 단언문
        assertNotNull(updatedReservation, "수정된 예약은 null이 아니어야 합니다.");
        assertEquals(modifyReservation.getMessage(), updatedReservation.getMessage(), "메시지가 일치해야 합니다.");
        // ... 필요한 추가 단언문

        // 데이터 정리 또는 @Transactional에 의존하여 롤백
    }


    @Test
    void testRequestCancelRoomBooking() {
        // 예약 생성
        ReservationDTO newReservation = reservationService.bookRoom(testReservation);
        Long rvno = newReservation.getRvno();

        // 예약자 이메일
        String userEmail = newReservation.getMemberDTO().getEmail();

        // 예약 취소 요청
        boolean requestCancelSuccess = reservationService.requestCancel(rvno, userEmail);

        // 취소 요청 성공 검증
        assertTrue(requestCancelSuccess, "예약 취소 요청이 성공적으로 이루어져야 합니다.");

        // 취소 요청 상태 검증
        ReservationDTO cancelledRequestReservation = reservationService.getRoomBookingDetails(rvno);
        assertEquals(ReservationStatus.CANCELLATION_REQUESTED, cancelledRequestReservation.getStatus(), "예약 상태가 취소 요청 상태여야 합니다.");
    }

    @Test
    void testApproveCancellationRoomBooking() {
        // 예약 생성 및 취소 요청 상태로 변경
        ReservationDTO newReservation = reservationService.bookRoom(testReservation);
        Long rvno = newReservation.getRvno();
        String userEmail = newReservation.getMemberDTO().getEmail();
        reservationService.requestCancel(rvno, userEmail);

        // 예약 취소 승인
        boolean approveCancelSuccess = reservationService.approveCancellation(rvno);

        // 취소 승인 성공 검증
        assertTrue(approveCancelSuccess, "예약 취소 승인이 성공적으로 이루어져야 합니다.");

        // 최종 취소 상태 검증
        ReservationDTO finalReservation = reservationService.getRoomBookingDetails(rvno);
        assertEquals(ReservationStatus.CANCELLED, finalReservation.getStatus(), "예약 상태가 최종적으로 취소된 상태여야 합니다.");
    }


  /*
    @Test
    @Transactional
    void testListUserRoomBookings() {
        String userEmail = "user@example.com"; // 테스트용 사용자 이메일
        List<ReservationDTO> bookings = reservationService.listUserRoomBookings(userEmail);
        assertNotNull(bookings, "사용자 예약 목록이 반환되어야 합니다.");
        // 추가적인 검증 로직
    }

    @Test
    @Transactional
    void testListAccommodationRoomBookings() {
        Long ano = 1L; // 테스트용 숙소 번호
        List<ReservationDTO> bookings = reservationService.listAccommodationRoomBookings(ano);
        assertNotNull(bookings, "숙소 예약 목록이 반환되어야 합니다.");
        // 추가적인 검증 로직
    }

    @Test
    @Transactional
    void testListRoomBookings() {
        Long rno = 1L; // 테스트용 방 번호
        List<ReservationDTO> bookings = reservationService.listRoomBookings(rno);
        assertNotNull(bookings, "방 예약 목록이 반환되어야 합니다.");
        // 추가적인 검증 로직
    }

    @Test
    @Transactional
    void testCheckRoomAvailability() {
        Long rno = 1L; // 테스트용 방 번호
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(1);
        boolean available = reservationService.checkRoomAvailability(rno, startDate, endDate);
        assertTrue(available, "방이 예약 가능해야 합니다.");
        // 추가적인 검증 로직
    }*/
}
