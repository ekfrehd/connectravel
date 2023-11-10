package com.connectravel.service;

import com.connectravel.constant.Role;
import com.connectravel.dto.QnaReplyDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoard;
import com.connectravel.entity.QnaReply;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.QnaBoardRepository;
import com.connectravel.repository.QnaReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class QnaReplyServiceTest {

    private static final Logger log = LoggerFactory.getLogger(QnaReplyServiceTest.class);

    @Autowired
    private QnaReplyService qnaReplyService;

    @Autowired
    private QnaReplyRepository qnaReplyRepository;

    @Autowired
    private QnaBoardRepository qnaBoardRepository;

    @Autowired
    private MemberRepository memberRepository;

    private QnaReplyDTO qnaReplyDTO;
    private QnaBoard savedQnaBoard;
    private Member savedMember;

    // 테스트에 필요한 기본 데이터 설정
    @BeforeEach
    public void setUp() {
        // 테스트 실행 시마다 다른 이메일을 사용합니다.
        String uniqueEmail = "replyTest+" + System.currentTimeMillis() + "@example.com";
        savedMember = memberRepository.save(Member.builder()
                .email(uniqueEmail)
                .password("password")
                .name("홍길동")
                .nickName("gildong")
                .tel("010-1234-5678")
                .point(1000)
                .role(Role.USER)
                .build());

        savedQnaBoard = qnaBoardRepository.save(QnaBoard.builder()
                .title("테스트 질문글")
                .content("테스트 내용입니다.")
                .member(savedMember)
                .build());

        qnaReplyDTO = new QnaReplyDTO();
        qnaReplyDTO.setQbno(savedQnaBoard.getQbno());
        qnaReplyDTO.setContent("테스트 댓글 내용입니다.");
        qnaReplyDTO.setReplyer(uniqueEmail);
    }

    @Test
    @Transactional
    public void testCreateQnaReply() {
        // QnA 댓글 등록 테스트
        Long qrno = qnaReplyService.createQnaReply(qnaReplyDTO);
        assertNotNull(qrno, "댓글 등록 후 ID를 반환해야 합니다.");

        QnaReply qnaReply = qnaReplyRepository.findById(qrno).orElse(null);
        assertNotNull(qnaReply, "저장된 댓글을 찾을 수 있어야 합니다.");
        assertEquals(qnaReplyDTO.getContent(), qnaReply.getContent(), "저장된 댓글의 내용이 일치해야 합니다.");

        log.debug("등록된 댓글 ID: {}", qrno);
        log.debug("등록된 댓글 내용: {}", qnaReply.getContent());
    }

    @Test
    public void testGetQnaRepliesByQbno() {
        // 특정 QnA 게시물의 댓글 목록 조회 테스트
        Long qrno = qnaReplyService.createQnaReply(qnaReplyDTO); // 댓글 먼저 생성
        List<QnaReplyDTO> replies = qnaReplyService.getQnaRepliesByQbno(savedQnaBoard.getQbno());

        assertNotNull(replies, "댓글 목록 조회 결과는 null이 아니어야 합니다.");
        assertTrue(replies.stream().anyMatch(reply -> reply.getQrno().equals(qrno)), "조회된 댓글 목록에 방금 생성한 댓글이 포함되어야 합니다.");

        replies.forEach(reply -> {
            log.debug("댓글 ID: {}", reply.getQrno());
            log.debug("댓글 내용: {}", reply.getContent());
        });
    }

    @Test
    @Transactional
    public void testUpdateQnaReply() {
        // QnA 댓글 수정 테스트
        Long qrno = qnaReplyService.createQnaReply(qnaReplyDTO); // 댓글 먼저 생성
        qnaReplyDTO.setQrno(qrno);
        qnaReplyDTO.setContent("수정된 댓글 내용입니다.");

        qnaReplyService.updateQnaReply(qnaReplyDTO);

        QnaReply updatedQnaReply = qnaReplyRepository.findById(qrno).orElse(null);
        assertNotNull(updatedQnaReply, "수정된 댓글을 찾을 수 있어야 합니다.");
        assertEquals("수정된 댓글 내용입니다.", updatedQnaReply.getContent(), "수정된 댓글의 내용이 일치해야 합니다.");

        log.debug("수정된 댓글 ID: {}", updatedQnaReply.getQrno());
        log.debug("수정된 댓글 내용: {}", updatedQnaReply.getContent());
    }

    @Test
    @Transactional
    public void testDeleteQnaReply() {
        // QnA 댓글 삭제 테스트
        Long qrno = qnaReplyService.createQnaReply(qnaReplyDTO); // 댓글 먼저 생성
        qnaReplyService.deleteQnaReply(qrno);

        boolean exists = qnaReplyRepository.existsById(qrno);
        assertFalse(exists, "댓글이 삭제된 후에는 더 이상 존재하지 않아야 합니다.");

        log.debug("삭제된 댓글 ID: {}", qrno);
    }
}