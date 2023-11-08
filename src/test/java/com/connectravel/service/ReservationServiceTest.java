package com.connectravel.service;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
                .email("testmember2@example.com")
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
                .accommodationType("호텔")
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
                .state(true)
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
    @Transactional
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
    // @Transactional
    void testModifyExistingRoomBooking() {
        // 수정할 예약 정보
        ReservationDTO modifyReservation = ReservationDTO.builder()
                .rvno(3L) // 데이터 베이스에 있는 예약 번호 설정
                .message("modify reservation")
                .money(150000)
                .numberOfGuests(4)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(2))
                .state(true)
                .roomDTO(testRoom) // 예약할 방의 정보
                .memberDTO(MemberDTO.builder() // 예약자의 정보
                        .id(testMember.getId())
                        .name(testMember.getName())
                        .nickName(testMember.getNickName())
                        .email(testMember.getEmail())
                        .build())
                .build();

        // 예약 수정 테스트
        ReservationDTO updatedReservation = reservationService.modifyRoomBooking(3L, modifyReservation);
        assertNotNull(updatedReservation, "수정된 예약이 반환되어야 합니다.");

        // 수정된 내용이 반영되었는지 확인
        assertEquals(modifyReservation.getMessage(), updatedReservation.getMessage(), "메시지가 수정되어야 합니다.");
        assertEquals(modifyReservation.getMoney(), updatedReservation.getMoney(), "가격이 수정되어야 합니다.");
        assertEquals(modifyReservation.getNumberOfGuests(), updatedReservation.getNumberOfGuests(), "손님 수가 수정되어야 합니다.");
        assertEquals(modifyReservation.getStartDate(), updatedReservation.getStartDate(), "시작 날짜가 수정되어야 합니다.");
        assertEquals(modifyReservation.getEndDate(), updatedReservation.getEndDate(), "종료 날짜가 수정되어야 합니다.");
        assertEquals(modifyReservation.isState(), updatedReservation.isState(), "상태가 수정되어야 합니다.");

        log.debug("Modified Existing Reservation : {}", updatedReservation);
    }


  /*

    @Test
    @Transactional
    void testCancelRoomBooking() {
        // 취소를 위한 예약 번호 준비
        Long rvno = 1L; // 테스트용 예약 번호

        // 예약 취소 테스트
        assertDoesNotThrow(() -> reservationService.cancelRoomBooking(rvno), "예약 취소 시 예외가 발생하지 않아야 합니다.");
    }

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
