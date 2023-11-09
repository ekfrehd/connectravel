package com.connectravel.service;

import com.connectravel.dto.ReviewReplyDTO;
import com.connectravel.entity.ReviewBoard;
import com.connectravel.entity.ReviewReply;
import com.connectravel.repository.ReviewBoardRepository;
import com.connectravel.repository.ReviewReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReviewReplyServiceTest {
    private static final Logger log = LoggerFactory.getLogger(ReviewReplyServiceTest.class);

    @Autowired
    private ReviewReplyService reviewReplyService;

    @Autowired
    private ReviewReplyRepository reviewReplyRepository;

    @Autowired
    private ReviewBoardRepository reviewBoardRepository;

    private ReviewReplyDTO testReviewReply;
    private ReviewBoard testReviewBoard;

    @BeforeEach
    public void setUp() {
        // 데이터베이스에 이미 존재하는 ReviewBoard의 ID를 가정
        Long rbno = 4L;
        testReviewBoard = reviewBoardRepository.findById(rbno)
                .orElseThrow(() -> new EntityNotFoundException("ReviewBoard를 찾을 수 없습니다."));

        // 테스트 ReviewReplyDTO 설정
        testReviewReply = ReviewReplyDTO.builder()
                .content("이용해주셔서 감사합니다!")
                .rbno(rbno)
                .replyer("testmember@example.com") // seller
                .build();
    }

    @Test
    @DisplayName("숙소 리뷰에 답글을 다는 테스트")
    public void testRegisterReviewReply() {
        // 리뷰 답글 등록
        Long rrno = reviewReplyService.createReviewReply(testReviewReply);

        // 영속화된 답글 검색
        ReviewReply persistedReply = reviewReplyRepository.findById(rrno)
                .orElseThrow(() -> new EntityNotFoundException("답글을 찾을 수 없습니다."));

        // 검증
        assertNotNull(rrno, "리뷰 답글 ID는 null이 아니어야 합니다.");
        assertNotNull(persistedReply, "영속화된 리뷰 답글은 null이 아니어야 합니다.");
        assertTrue(persistedReply.getContent().equals(testReviewReply.getContent()), "답글 내용은 DTO와 일치해야 합니다.");

        // 영속화된 답글의 상세 정보를 보여주는 디버그 로그
        log.debug("영속화된 ReviewReply ID: {}, 내용: '{}', 답변자: '{}'",
                persistedReply.getRrno(), persistedReply.getContent(), persistedReply.getMember().getEmail());

    }

    @Test
    @DisplayName("리뷰 답글 수정 테스트")
    public void testUpdateReviewReply() {
        Long replyId = setUpReply(); // 리뷰 답글 세팅

        ReviewReplyDTO updatedReply = ReviewReplyDTO.builder()
                .rrno(replyId)
                .content("답글 수정 테스트 진행")
                .rbno(testReviewBoard.getRbno())
                .replyer("testmember@example.com")
                .build();

        reviewReplyService.updateReviewReply(updatedReply);

        ReviewReply updatedEntity = reviewReplyRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("답글을 찾을 수 없습니다."));
        assertNotNull(updatedEntity, "수정된 리뷰 답글은 null이 아니어야 합니다.");
        assertEquals("답글 수정 테스트 진행", updatedEntity.getContent(), "답글 내용은 수정된 내용과 일치해야 합니다.");

    }

    @Test
    @DisplayName("특정 리뷰에 대한 답글 리스트 조회 테스트")
    public void testGetRepliesByReviewRbno() {
        Long rbno = testReviewBoard.getRbno();

        List<ReviewReplyDTO> replies = reviewReplyService.getRepliesByReviewRbno(rbno);

        assertFalse(replies.isEmpty(), "답글 리스트는 비어있지 않아야 합니다.");
        assertTrue(replies.stream().anyMatch(r -> r.getRbno().equals(rbno)), "모든 답글은 주어진 리뷰 ID와 연결되어야 합니다.");

    }

    @Test
    @DisplayName("리뷰 답글 삭제 테스트")
    public void testDeleteReviewReply() {
        Long replyId = setUpReply(); // 리뷰 답글 세팅

        reviewReplyService.deleteReviewReply(replyId);

        Optional<ReviewReply> deletedReply = reviewReplyRepository.findById(replyId);
        assertTrue(deletedReply.isEmpty(), "삭제 후에는 리뷰 답글을 찾을 수 없어야 합니다.");
    }

    // 리뷰 답글을 데이터베이스에 세팅하고 ID 반환하는 헬퍼 메서드
    private Long setUpReply() {
        ReviewReplyDTO setupReply = ReviewReplyDTO.builder()
                .content("초기 리뷰 답글")
                .rbno(testReviewBoard.getRbno())
                .replyer("testmember@example.com") // 이메일도 실제 테스트 환경에 맞게 조정해야 함
                .build();
        return reviewReplyService.createReviewReply(setupReply);
    }

}
