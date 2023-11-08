package com.connectravel.service;

import com.connectravel.dto.ReviewBoardDTO;
import com.connectravel.entity.Reservation;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ReservationRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ReviewBoardServiceTest {

    @Autowired
    private ReviewBoardService reviewBoardService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;  // 회원 정보를 가져오기 위한 의존성 주입

    @Test
    @Transactional
    public void testRegister() throws NotFoundException {
        // 기존의 예약 정보 가져오기
        Long reservationId = 3L;  // 가정된 예약 ID
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        // 회원 이메일 검증
        String writerEmail = "testmember@example.com";  // 데이터베이스에 존재하는 회원 이메일
        boolean memberExists = memberRepository.findByEmail(writerEmail).isPresent();
        if (!memberExists) {
            throw new NotFoundException("Member not found");
        }

        // DTO 생성
        ReviewBoardDTO dto = ReviewBoardDTO.builder()
                .grade(5d)
                .content("Test...")
                .rno(reservation.getRoom().getRno())  // 예약 정보에서 방 번호를 가져옵니다.
                .ano(35L)  // 숙소 번호는 예제로 1L로 설정했습니다.
                .rvno(reservationId)  // 예약 ID를 설정합니다.
                .writerEmail(writerEmail)
                .build();

        // 후기 등록 테스트
        Long rbno = reviewBoardService.createReview(dto);

        // 테스트 검증
        assertNotNull(rbno, "The review must be registered and return a review board number");
    }
}

