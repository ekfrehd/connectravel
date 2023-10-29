package com.connectravel.repository;

import com.connectravel.dto.MemberFormDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoard;
import com.connectravel.service.QnaBoardService;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class QnaBoardRepositoryTests {

    @Autowired
    private QnaBoardRepository qnaBoardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QnaBoardService qnaBoardService;
    @Autowired
    private QnaReplyRepository qnaReplyRepository;

    @Test // 게시글만 추가 테스트
    public void testInsert () {
        IntStream.rangeClosed (1, 10).forEach (i -> {//1부터 10까지 생성
            QnaBoard qnaBoard = QnaBoard.builder ()
                    .title ("QnA 게시글 더미데이터" + i)
                    .content ("QnA 게시글 내용 더미데이터" + i)
            .build ();
            QnaBoard result = qnaBoardRepository.save (qnaBoard);
            System.out.println("BNO: " + result.getBno ());
        });
    }

    private Member createMember () {
        MemberFormDTO memberFormDTO = new MemberFormDTO ();
        memberFormDTO.setName ("더미봇");
        memberFormDTO.setEmail ("sample@sample.com");
        memberFormDTO.setNickName("더미봇");
        memberFormDTO.setTel("010-0000-0000");
        memberFormDTO.setPassword("1234");
        return Member.createMember (memberFormDTO);
    }

    @Test //특정 회원의 게시글 추가 테스트
    public void testInsertAddMember () {
        Member member = createMember ();
        memberRepository.save (member);

        IntStream.rangeClosed (1, 10).forEach (i -> {//1부터 10까지 생성
            QnaBoard qnaBoard = QnaBoard.builder ()
                    .title ("QnA 게시글 더미데이터" + i)
                    .content ("QnA 게시글 내용 더미데이터" + i)
                    .member(member)
                    .build ();
            QnaBoard result = qnaBoardRepository.save (qnaBoard);
            System.out.println("BNO: " + result.getBno ());
        });
    }

    @Test // 게시글 조회 테스트
    public void testReadOne () {
        Long bno = 21L; // 존재하는 게시글 입력
        qnaBoardService.get(bno);
        System.out.println(qnaBoardService.get(bno));
    }

    @Test // 게시글 수정 테스트
    public void testUpdate () {
        Long bno = 150L;//n번째 게시글, 존재하는 게시글 입력
        //Optional 객체는 null 에러 방지
        Optional<QnaBoard> result = qnaBoardRepository.findById (bno);
        //result가 null일 경우 board에 담는다.
        QnaBoard qnaBoard = result.orElseThrow (() -> new NoSuchElementException ("게시글이 존재하지 않습니다."));
        //board 클래스의 change 메서드 실행
        qnaBoard.changeTitle("게시글 제목 수정");
        qnaBoard.changeContent("게시글 내용 수정");
        //업데이트 내용 DB 저장
        qnaBoardRepository.save (qnaBoard);
    }

    @Test // 게시글 삭제 테스트, 댓글이 있으면 안지워짐
    public void testDelete () {
        Long bno = 10L;
        qnaBoardRepository.deleteById (bno);
    }

    @Test // 게시글 삭제 테스트
    public void testDeleteAddReply () {
        Long bno = 10L;
        //qnaBoardRepository.deleteById (bno);
    }
}










