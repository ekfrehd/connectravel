package com.connectravel.service;

import com.connectravel.constant.Role;
import com.connectravel.dto.TourBoardReviewReplyDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.TourBoardReview;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.TourBoardReviewReplyRepository;
import com.connectravel.repository.TourBoardReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TourBoardReviewReplyServiceTest {

    @Autowired
    private TourBoardReviewReplyService tourBoardReviewReplyService;
    @Autowired
    private TourBoardReviewReplyRepository tourBoardReviewReplyRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TourBoardReviewRepository tourBoardReviewRepository;

    private TourBoardReviewReplyDTO testTourBoardReviewReplyDTO;
    private String testEmail; // 테스트용 이메일 변수
    private Long testTourBoardReviewId; // 테스트용 TourBoardReview ID

    @BeforeEach
    public void setUp() {
        // 고유한 이메일 생성
        String uniqueEmail = "replyTest+" + System.currentTimeMillis() + "@example.com";
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

        // TourBoardReview 객체 생성 및 저장
        TourBoardReview tourBoardReview = tourBoardReviewRepository.save(TourBoardReview.builder()
                .content("Test Review Content")
                .grade(5.0)
                .member(member)
                .build());
        testTourBoardReviewId = tourBoardReview.getTbrno();

        // TourBoardReviewReplyDTO 객체 초기화
        testTourBoardReviewReplyDTO = TourBoardReviewReplyDTO.builder()
                .content("Test Reply Content")
                .replyer(member.getEmail())
                .tbrno(tourBoardReview.getTbrno())
                .build();
    }

    @Test
    @DisplayName("TourBoardReviewReply 생성 테스트")
    void createTourBoardReviewReplyTest() {
        Long tbrrno = tourBoardReviewReplyService.createTourBoardReviewReply(testTourBoardReviewReplyDTO);
        assertNotNull(tbrrno, "생성된 TourBoardReviewReply의 ID는 null이 아니어야 합니다.");
    }

    @Test
    @DisplayName("TourBoardReviewReply 수정 테스트")
    void updateTourBoardReviewReplyTest() {
        Long tbrrno = tourBoardReviewReplyService.createTourBoardReviewReply(testTourBoardReviewReplyDTO);
        testTourBoardReviewReplyDTO.setTbrrno(tbrrno);
        testTourBoardReviewReplyDTO.setContent("Updated Reply Content");

        tourBoardReviewReplyService.updateTourBoardReviewReply(testTourBoardReviewReplyDTO);

        TourBoardReviewReplyDTO updatedDTO = tourBoardReviewReplyService.getTourBoardReviewReply(tbrrno);
        assertNotNull(updatedDTO, "수정된 TourBoardReviewReply는 null이 아니어야 합니다.");
        assertEquals("Updated Reply Content", updatedDTO.getContent(), "TourBoardReviewReply 내용이 업데이트 되어야 합니다.");
    }

    @Test
    @DisplayName("TourBoardReviewReply 삭제 테스트")
    void deleteTourBoardReviewReplyTest() {
        // TourBoardReviewReply 생성
        Long tbrrno = tourBoardReviewReplyService.createTourBoardReviewReply(testTourBoardReviewReplyDTO);

        // TourBoardReviewReply 삭제
        tourBoardReviewReplyService.deleteTourBoardReviewReply(tbrrno);

        // 삭제된 TourBoardReviewReply 조회 시 예외 발생 확인
        assertThrows(EntityNotFoundException.class, () -> {
            tourBoardReviewReplyService.getTourBoardReviewReply(tbrrno);
        });
    }
}
