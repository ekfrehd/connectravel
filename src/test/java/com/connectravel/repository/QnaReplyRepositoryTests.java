package com.connectravel.repository;

import com.connectravel.dto.QnaReplyDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoard;
import com.connectravel.entity.QnaReply;
import com.connectravel.service.QnaReplyService;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest//테스트 클래스
@Log4j2//로그 사용
public class QnaReplyRepositoryTests {

    @Autowired//객체 생성
    private QnaReplyRepository qnaReplyRepository;
    @Autowired//객체 생성
    private QnaBoardRepository qnaBoardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QnaReplyService qnaReplyService;

    @Test //특정 회원의 댓글 추가 테스트
    public void testQnaReplyInsertAddMember () {
        System.out.println ("특정 회원 댓글 추가");
        Optional<QnaBoard> result = qnaBoardRepository.findById (21L);
        QnaBoard qnaBoard = result.orElseThrow (() -> new NoSuchElementException ("게시글이 존재하지 않습니다."));
        Member member = memberRepository.findByEmail ("sample@sample.com");

        IntStream.rangeClosed (1, 10).forEach (i -> {//1부터 10까지 생성
            QnaReply qnaReply = QnaReply.builder ().text ("QnA 게시글 댓글 더미데이터").qnaBoard (qnaBoard).member (member).build ();
            QnaReply result1 = qnaReplyRepository.save (qnaReply);
            System.out.println ("BNO: " + result1.getRno ());
        });
    }

    @Test // 댓글 조회 테스트
    public void testQnaReplyReadOne () {
        Long rno = 2L; // 단일 댓글 조회
        qnaReplyRepository.findById (rno);
        System.out.println ("단일 댓글 확인 : " + qnaReplyRepository.findById (rno));

        Long bno = 21L; // 게시글에 존재하는 댓글 조회
        List<QnaReplyDTO> result = qnaReplyService.getList (bno);
        System.out.println ("특정 게시글의 댓글 확인 : " + result);
    }

    @Test // 게시글 수정 테스트
    public void testQnaReplyUpdate () {
        Long rno = 2L; // 단일 댓글 조회
        Optional<QnaReply> result = qnaReplyRepository.findById (rno);
        QnaReply qnaReply = result.orElseThrow (() -> new NoSuchElementException ("게시글이 존재하지 않습니다."));
        qnaReply.changeText ("댓글 내용 수정");
        qnaReplyRepository.save (qnaReply);
    }

    @Test // 게시글 삭제 테스트
    public void testQnaReplyDelete () {
        System.out.println ("댓글 삭제");
        Long rno = 2L;
        qnaReplyRepository.deleteById (rno);
    }
}
