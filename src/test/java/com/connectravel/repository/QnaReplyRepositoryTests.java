package com.connectravel.repository;

import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoard;
import com.connectravel.entity.QnaReply;
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

    @Test //특정 회원의 댓글 추가 테스트
    public void testQnaReplyInsertAddMember() {
        Optional<QnaBoard> result = qnaBoardRepository.findById(1L); // 존재하는 댓글 입력, 정보 찾아 저장
        QnaBoard qnaBoard = result.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다.")); // 예외처리
        Member member = memberRepository.findByEmail("sample@sample.com"); // 회원 정보 입력

        IntStream.rangeClosed(1, 10).forEach(i -> { // 1부터 10까지 생성
            QnaReply qnaReply = QnaReply.builder()
                    .text("QnA 게시글 댓글 더미데이터" + i) // 댓글 내용
                    .qnaBoard(qnaBoard) // 게시글 참조
                    .member(member) // 회원 참조
                    .build();

            QnaReply result1 = qnaReplyRepository.save(qnaReply); // DB 반영
            System.out.println("댓글 번호 : " + result1.getRno());
            System.out.println("댓글 내용 : " + result1.getText());
        });
    }

    @Test // 댓글 조회 테스트
    public void testQnaReplyReadOne() {
        Long rno = 10L; // 존재하는 댓글 입력
        qnaReplyRepository.findById(rno); // 해당 번호 댓글 조회
        System.out.println("단일 댓글 확인 : " + qnaReplyRepository.findById(rno));

        Optional<QnaBoard> resultQnaBoard = qnaBoardRepository.findById(1L); // 게시물에 존재하는 댓글 리스트 조회
        QnaBoard qnaBoard = resultQnaBoard.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다.")); // 예외처리
        List<QnaReply> result = qnaReplyRepository.getRepliesByQnaBoardOrderByRno(qnaBoard); // QnaBoard에 있는 댓글들을 Rnp 기준 정렬하여 가져온다.

        for (QnaReply reply : result) { // 댓글 출력
            System.out.println(reply.getQnaBoard().getBno() + "번 게시글, " + reply.getRno() + "번 댓글, " + reply.getText());
        }
    }

    @Test // 댓글 수정 테스트
    public void testQnaReplyUpdate() {
        Long rno = 10L; // 존재하는 댓글 입력
        Optional<QnaReply> result = qnaReplyRepository.findById(rno); // 해당 번호 댓글 조회
        QnaReply qnaReply = result.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다.")); // 예외 처리
        qnaReply.changeText("댓글 내용 수정11"); // 댓글 내용 수정
        qnaReply = qnaReplyRepository.save(qnaReply); // DB 반영

        System.out.println("수정한 댓글 정보 : " + qnaReply);
    }

    @Test // 게시글 삭제 테스트
    public void testQnaReplyDelete() {
        Long rno = 10L; // 삭제할 댓글 입력
        Optional<QnaReply> optionalQnaReply = qnaReplyRepository.findById(rno); // 댓글 정보 조회
        QnaReply qnaReply = optionalQnaReply.orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다.")); // 예외 처리

        qnaReplyRepository.deleteById(qnaReply.getRno()); // 댓글이 존재하면 삭제 처리
    }
}