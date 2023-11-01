package com.connectravel.service;

import com.connectravel.dto.QnaBoardDTO;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class QnaBoardServiceTests {

    @Autowired
    private QnaBoardService qnaBoardService;

    @Test // 게시글 추가 테스트
    public void testRegisterQnaBoard() {
        // 테스트 QnaBoardDTO 생성
        QnaBoardDTO qnaBoardDTO = new QnaBoardDTO();
        qnaBoardDTO.setTitle("테스트 제목");
        qnaBoardDTO.setContent("테스트 내용");
        qnaBoardDTO.setWriterEmail("sample@sample.com");

        // 게시글 등록 register 메서드 호출
        Long bno = qnaBoardService.register(qnaBoardDTO);

        System.out.println(bno + "번 게시물 등록");
    }

    @Test
    public void testGetQnaBoard() {
        Long bno = 1L; // 테스트 게시글 번호

        // 게시글 조회 get 메서드 호출
        QnaBoardDTO result = qnaBoardService.get(bno);

        System.out.println(bno + "번 게시글");
        System.out.println("QnaBoard 정보: " + result.toString());
        System.out.println("Member 정보: " + result.getWriterEmail() + ", " + result.getWriterName());
    }

    @Test
    @Transactional
    public void testModifyQnaBoard() {
        Long bno = 1L; // 수정할 게시글 번호

        // 테스트 QnaBoardDTO 생성 (수정할 내용 포함)
        QnaBoardDTO qnaBoardDTO = new QnaBoardDTO();
        qnaBoardDTO.setBno(bno);
        qnaBoardDTO.setTitle("수정된 제목");
        qnaBoardDTO.setContent("수정된 내용");

        // 게시글 수정
        qnaBoardService.modify(qnaBoardDTO);

        // 수정한 게시글 가져오기
        QnaBoardDTO result = qnaBoardService.get(bno);
        System.out.println("QnaBoard 정보: " + result.toString());
    }

    @Test
    public void testRemoveBoard() {
        Long bno = 16L; // 삭제할 게시글 번호

        // 게시글 삭제 get 메서드 호출
        qnaBoardService.removeWithReplies(bno);
    }
}










