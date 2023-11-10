package com.connectravel.service;

import com.connectravel.constant.Role;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.QnaBoardDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoard;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.QnaBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class QnaBoardServiceTest {

    private static final Logger log = LoggerFactory.getLogger(QnaBoardServiceTest.class);

    @Autowired
    private QnaBoardService qnaBoardService;

    @Autowired
    private QnaBoardRepository qnaBoardRepository;

    @Autowired
    private MemberRepository memberRepository;

    private QnaBoardDTO qnaBoardDTO;

    // 테스트에 필요한 기본 데이터 설정
    @BeforeEach
    public void setUp() {
        // 테스트 실행 시마다 다른 이메일을 사용합니다.
        String uniqueEmail = "qnaTest+" + System.currentTimeMillis() + "@example.com";
        Member member = memberRepository.save(Member.builder()
                .email(uniqueEmail)
                .password("password")
                .name("홍길동")
                .nickName("gildong")
                .tel("010-1234-5678")
                .point(1000)
                .role(Role.USER)
                .build());

        QnaBoard qnaBoard = qnaBoardRepository.save(QnaBoard.builder()
                .title("테스트 질문글")
                .content("테스트 내용입니다.")
                .member(member)
                .build());

        qnaBoardDTO = new QnaBoardDTO();
        qnaBoardDTO.setQbno(qnaBoard.getQbno());
        qnaBoardDTO.setTitle(qnaBoard.getTitle());
        qnaBoardDTO.setContent(qnaBoard.getContent());
        qnaBoardDTO.setWriterEmail(member.getEmail());
    }

    @Test
    @Transactional
    public void testCreateQna() {
        // QnA 게시글 등록 테스트
        Long qbno = qnaBoardService.createQna(qnaBoardDTO);
        assertNotNull(qbno, "게시글 등록 후 ID를 반환해야 합니다.");

        QnaBoard qnaBoard = qnaBoardRepository.findById(qbno).orElse(null);
        assertNotNull(qnaBoard, "저장된 게시글을 찾을 수 있어야 합니다.");
        assertEquals(qnaBoardDTO.getTitle(), qnaBoard.getTitle(), "저장된 게시글의 제목이 일치해야 합니다.");

        log.debug("등록된 게시글 ID: {}", qbno);
        log.debug("등록된 게시글 제목: {}", qnaBoard.getTitle());
    }

    @Test
    public void testGetQnaByQbno() {
        // QnA 게시글 조회 테스트
        QnaBoardDTO foundQnaBoardDTO = qnaBoardService.getQnaByQbno(qnaBoardDTO.getQbno());
        assertNotNull(foundQnaBoardDTO, "게시글 조회 결과는 null이 아니어야 합니다.");
        assertEquals(qnaBoardDTO.getTitle(), foundQnaBoardDTO.getTitle(), "조회된 게시글의 제목이 일치해야 합니다.");

        log.debug("조회된 게시글 ID: {}", foundQnaBoardDTO.getQbno());
        log.debug("조회된 게시글 제목: {}", foundQnaBoardDTO.getTitle());
    }

    @Test
    @Transactional
    public void testUpdateQna() {
        // QnA 게시글 수정 테스트
        qnaBoardDTO.setTitle("제목 수정 테스트");
        qnaBoardService.updateQna(qnaBoardDTO);

        QnaBoard updatedQnaBoard = qnaBoardRepository.findById(qnaBoardDTO.getQbno()).orElse(null);
        assertNotNull(updatedQnaBoard, "수정된 게시글을 찾을 수 있어야 합니다.");
        assertEquals("제목 수정 테스트", updatedQnaBoard.getTitle(), "수정된 게시글의 제목이 일치해야 합니다.");

        log.debug("수정된 게시글 ID: {}", updatedQnaBoard.getQbno());
        log.debug("수정된 게시글 제목: {}", updatedQnaBoard.getTitle());
    }

    @Test
    @Transactional
    public void testDeleteQnaWithReplies() {
        // QnA 게시글과 답변 삭제 테스트
        qnaBoardService.deleteQnaWithReplies(qnaBoardDTO.getQbno());
        boolean exists = qnaBoardRepository.existsById(qnaBoardDTO.getQbno());
        assertFalse(exists, "게시글이 삭제된 후에는 더 이상 존재하지 않아야 합니다.");

        log.debug("삭제된 게시글 ID: {}", qnaBoardDTO.getQbno());
    }

    @Test
    public void testGetPaginatedQnas() {
        // QnA 게시글 페이지네이션 목록 조회 테스트
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(1);
        pageRequestDTO.setSize(10);

        PageResultDTO<QnaBoardDTO, Object[]> pageResultDTO = qnaBoardService.getPaginatedQnas(pageRequestDTO);

        assertNotNull(pageResultDTO, "페이지 결과는 null이 아니어야 합니다.");
        assertTrue(pageResultDTO.getDtoList().size() > 0, "조회된 페이지의 게시글 수는 0보다 커야 합니다.");

        log.debug("총 페이지 수: {}", pageResultDTO.getTotalPage());
        log.debug("현재 페이지 게시글 수: {}", pageResultDTO.getDtoList().size());
        pageResultDTO.getDtoList().forEach(dto -> log.debug("게시글 제목: {}", dto.getTitle()));
    }
}
