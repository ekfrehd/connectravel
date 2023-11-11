package com.connectravel.service;

import com.connectravel.constant.Role;
import com.connectravel.dto.TourBoardReviewDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.TourBoard;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.TourBoardRepository;
import com.connectravel.repository.TourBoardReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TourBoardReviewServiceTest {

    @Autowired
    private TourBoardReviewService tourBoardReviewService;
    @Autowired
    private TourBoardRepository tourBoardRepository;
    @Autowired
    private MemberRepository memberRepository;

    private TourBoardReviewDTO testTourBoardReviewDTO;
    private String testEmail; // 테스트용 이메일 변수

    @BeforeEach
    public void setUp() {
        // 고유한 이메일 생성
        String uniqueEmail = "tourTest+" + System.currentTimeMillis() + "@example.com";
        testEmail = uniqueEmail; // 이메일 변수 저장

        // 멤버 객체 생성 및 저장
        Member member = memberRepository.save(Member.builder()
                .email(uniqueEmail)
                .password("password")
                .name("홍길동")
                .nickName("gildong")
                .tel("010-1234-5678")
                .point(1000)
                .role(Role.USER)
                .build());

        // TourBoard 객체 생성 및 저장
        TourBoard tourBoard = tourBoardRepository.save(TourBoard.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .region("테스트 지역")
                .category("테스트 카테고리")
                .address("테스트 주소")
                .postal(12345)
                .build());

        // TourBoardReviewDTO 객체 초기화
        testTourBoardReviewDTO = TourBoardReviewDTO.builder()
                .content("Test Review Content")
                .grade(5.0)
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .tbno(tourBoard.getTbno())
                .build();
    }


    @Test
    @DisplayName("TourBoardReview 생성 테스트")
    void createTourBoardReviewTest() {
        Long tbrno = tourBoardReviewService.createTourBoardReview(testTourBoardReviewDTO, testEmail);
        assertNotNull(tbrno, "생성된 TourBoardReview의 ID는 null이 아니어야 합니다.");
    }

    @Test
    @DisplayName("TourBoardReview 조회 테스트")
    void getTourBoardReviewTest() {
        Long tbrno = tourBoardReviewService.createTourBoardReview(testTourBoardReviewDTO, testEmail);
        TourBoardReviewDTO foundDTO = tourBoardReviewService.getTourBoardReview(tbrno);
        assertNotNull(foundDTO, "조회된 TourBoardReview는 null이 아니어야 합니다.");
        assertEquals(testTourBoardReviewDTO.getContent(), foundDTO.getContent(), "내용이 일치해야 합니다.");
    }

    @Test
    @DisplayName("TourBoardReview 수정 테스트")
    void updateTourBoardReviewTest() {
        Long tbrno = tourBoardReviewService.createTourBoardReview(testTourBoardReviewDTO, testEmail);
        testTourBoardReviewDTO.setTbrno(tbrno);
        testTourBoardReviewDTO.setContent("Updated Content");

        tourBoardReviewService.updateTourBoardReview(testTourBoardReviewDTO);

        TourBoardReviewDTO updatedDTO = tourBoardReviewService.getTourBoardReview(tbrno);
        assertNotNull(updatedDTO, "수정된 TourBoardReview는 null이 아니어야 합니다.");
        assertEquals("Updated Content", updatedDTO.getContent(), "TourBoardReview 내용이 업데이트 되어야 합니다.");
    }

    @Test
    @DisplayName("TourBoardReview와 관련된 Reply 삭제 테스트")
    void deleteTourBoardReviewWithRepliesTest() {
        // TourBoardReview 생성
        Long tbrno = tourBoardReviewService.createTourBoardReview(testTourBoardReviewDTO, testEmail);

        // TourBoardReview 삭제
        tourBoardReviewService.deleteTourBoardReviewWithReplies(tbrno);

        // 삭제된 TourBoardReview 조회 시 예외 발생 확인
        assertThrows(EntityNotFoundException.class, () -> {
            tourBoardReviewService.getTourBoardReview(tbrno);
        });

    }


}
