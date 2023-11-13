package com.connectravel.repository;

import com.connectravel.entity.Room;
import com.connectravel.entity.Reservation;
import com.connectravel.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ReservationRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(ReservationRepositoryTest.class);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .name("테스트 회원")
                .email("test@connectravel.com")
                .password("test1234")
                .nickName("testNick")
                .tel("010-1234-5678")
                .build();
        entityManager.persist(member);

        Room room = Room.builder()
                .roomName("테스트 방")
                .price(10000)
                .minimumOccupancy(1) // 최소 인원 설정
                .maximumOccupancy(2) // 최대 인원 설정
                .operating(true)
                .content("테스트 방 내용")
                .build();
        entityManager.persist(room);

        Reservation reservation = Reservation.builder()
                .room(room)
                .member(member)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .message("테스트 요청사항")
                .money(10000)
                .build();
        entityManager.persist(reservation);
    }

    @Test
    void saveReservation() {
        Room roomForReservation = roomRepository.findByRoomName("테스트 방").orElse(null);
        Member memberForReservation = memberRepository.findByEmail("test@connectravel.com").orElse(null);

        Reservation newReservation = Reservation.builder()
                .room(roomForReservation)
                .member(memberForReservation)
                .numberOfGuests(2) // 예약 인원 수 설정
                .startDate(LocalDate.now().plusDays(2))
                .endDate(LocalDate.now().plusDays(3))
                .message("새 예약 요청사항")
                .money(15000)
                .build();

        Reservation savedReservation = reservationRepository.save(newReservation);

        log.info("Saved Reservation: {}", savedReservation);

        // 검증
        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getStartDate()).isEqualTo(newReservation.getStartDate());
    }

    @Test // 회원의 email로 예약 조회
    void findReservationByMember() {
        Member memberForSearch = memberRepository.findByEmail("test@connectravel.com").orElse(null);
        Reservation foundReservation = reservationRepository.findByMember(memberForSearch).orElse(null);
        
        log.info("Found Reservation for Member {}: {}", memberForSearch.getEmail(), foundReservation);

        assertThat(foundReservation.getMember()).isEqualTo(memberForSearch);
    }

    @Test // 예약 번호로 예악 정보 수정 테스트 (퇴실 날짜 +1일)
    void updateReservation() {
        Reservation existingReservation = reservationRepository.findById(1L).orElse(null);
        existingReservation.setEndDate(LocalDate.now().plusDays(2));

        Reservation updatedReservation = reservationRepository.save(existingReservation);

        log.info("Updated Reservation: {}", updatedReservation);

        assertThat(updatedReservation.getEndDate()).isEqualTo(existingReservation.getEndDate());
    }

    @Test // 예약 번호를 기반으로 해당 예약 정보를 삭제하는 테스트
    void deleteReservation() {
        Reservation reservationToDelete = reservationRepository.findById(1L).orElse(null);

        // 예약한 사람 호출
        Member memberForDeletion = reservationToDelete.getMember();

        reservationRepository.delete(reservationToDelete);

        Reservation deletedReservation = reservationRepository.findById(1L).orElse(null);

        log.info("Deleted Reservation for Member {}: {}", memberForDeletion.getEmail(), deletedReservation);

        assertThat(deletedReservation).isNull();
    }


}
