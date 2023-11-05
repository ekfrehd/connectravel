package com.connectravel.service;

import com.connectravel.dto.QnaReplyDTO;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class QnaReplyServiceTests {

    @Autowired
    private QnaReplyService qnaReplyService;

    @Test // 댓글 추가 테스트
    public void testRegisterQnaReply() {

        QnaReplyDTO qnaReplyDTO = new QnaReplyDTO();
        qnaReplyDTO.setText("테스트 답변 내용");
        qnaReplyDTO.setBno(1L);
        qnaReplyDTO.setReplyer("더미봇");

        // 댓글 등록
        Long generatedRno = qnaReplyService.register(qnaReplyDTO);

        System.out.println(generatedRno + "번 댓글 등록");
    }

    @Test // 댓글 리스트 조회 테스트
    public void testGetListQnaReplies() {
        Long bno = 1L; // 가져올 게시글 번호

        // 해당 게시글의 답변 목록 가져오기
        List<QnaReplyDTO> replies = qnaReplyService.getList(bno);
        for (QnaReplyDTO reply : replies) {
            System.out.println(reply);
        }
    }

    @Test // 댓글 수정 테스트
    public void testModify() {

        QnaReplyDTO qnaReplyDTO = new QnaReplyDTO();
        qnaReplyDTO.setText("테스트 댓글 수정 1");
        qnaReplyDTO.setRno(12L);
        qnaReplyDTO.setReplyer("더미봇");

        qnaReplyService.modify(qnaReplyDTO);
    }

    @Test // 댓글 삭제 테스트
    public void testRemove() {
        QnaReplyDTO qnaReplyDTO = new QnaReplyDTO();
        qnaReplyDTO.setRno(13L);
        qnaReplyService.remove(qnaReplyDTO.getRno());
    }
}









