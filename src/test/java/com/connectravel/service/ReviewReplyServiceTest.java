package com.connectravel.service;

import com.connectravel.dto.ReviewReplyDTO;
import com.connectravel.entity.ReviewBoard;
import com.connectravel.entity.ReviewReply;
import com.connectravel.repository.ReviewBoardRepository;
import com.connectravel.repository.ReviewReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ReviewReplyServiceTest {

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
        Long rbno = 1L;
        testReviewBoard = reviewBoardRepository.findById(rbno)
                .orElseThrow(() -> new EntityNotFoundException("ReviewBoard를 찾을 수 없습니다."));

        // 테스트 ReviewReplyDTO 설정
        testReviewReply = ReviewReplyDTO.builder()
                .content("좋은 경험이었습니다!")
                .rbno(rbno)
                .replyer("testmember@example.com")
                .build();
    }

    @Test
    public void testRegisterReviewReply() {
        // 리뷰 답글 등록
        Long replyId = reviewReplyService.register(testReviewReply);

        // 영속화된 답글 검색
        ReviewReply persistedReply = reviewReplyRepository.findById(replyId)
                .orElseThrow(() -> new EntityNotFoundException("답글을 찾을 수 없습니다."));

        // 검증
        assertNotNull(replyId, "리뷰 답글 ID는 null이 아니어야 합니다.");
        assertNotNull(persistedReply, "영속화된 리뷰 답글은 null이 아니어야 합니다.");
        assertTrue(persistedReply.getContent().equals(testReviewReply.getContent()), "답글 내용은 DTO와 일치해야 합니다.");
    }
}
