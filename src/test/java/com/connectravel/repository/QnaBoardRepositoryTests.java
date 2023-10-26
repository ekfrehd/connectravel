package com.connectravel.repository;

import com.connectravel.dto.MemberFormDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoardEntity;
import com.connectravel.service.QnaBoardService;
import com.sun.istack.logging.Logger;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Test//게시글만 추가 테스트
    public void testInsert () {
        IntStream.rangeClosed (1, 10).forEach (i -> {//1부터 10까지 생성
            QnaBoardEntity qnaBoard = QnaBoardEntity.builder ()
                    .title ("title..." + i)
                    .content ("content..." + i)
            .build ();
            QnaBoardEntity result = qnaBoardRepository.save (qnaBoard);
            System.out.println("BNO: " + result.getBno ());
        });
    }

    //특정 회원의 게시글 추가 테스트
    private Member createMember () {
        MemberFormDTO memberFormDTO = new MemberFormDTO ();
        memberFormDTO.setName ("22");
        memberFormDTO.setEmail ("sample@example.com");
        memberFormDTO.setNickName("22");
        memberFormDTO.setTel("22-22");
        memberFormDTO.setPassword("1234");
        return Member.createMember (memberFormDTO);
    }

    @Test
    public void testInsertAddMember () {
        Member member = createMember ();
        memberRepository.save (member);

        IntStream.rangeClosed (1, 10).forEach (i -> {//1부터 10까지 생성
            QnaBoardEntity qnaBoard = QnaBoardEntity.builder ()
                    .title ("title..." + i)
                    .content ("content..." + i)
                    .member(member)
                    .build ();
            QnaBoardEntity result = qnaBoardRepository.save (qnaBoard);
            System.out.println("BNO: " + result.getBno ());
        });
    }

    @Test // 게시글 조회 테스트
    public void testReadOne () {
        Long bno = 21L; // 존재하는 게시글 입력
        qnaBoardService.get(bno);
        System.out.println(qnaBoardService.get(bno));
    }

    @Test//게시글 수정 테스트
    public void testUpdate () {
        Long bno = 22L;//n번째 게시글, 존재하는 게시글 입력
        //boardRepository을 사용하여 n번째 게시글을 찾는다.
        //Optional 객체는 null 에러 방지
        Optional<QnaBoardEntity> result = qnaBoardRepository.findById (bno);
        //result가 null일 경우 board에 담는다.
        QnaBoardEntity qnaBoard = result.orElseThrow ();
        //board 클래스의 change 메서드 실행
        qnaBoard.changeTitle("게시글 제목 수정22");
        qnaBoard.changeContent("게시글 내용 수정22");
        //업데이트 내용 DB 저장
        qnaBoardRepository.save (qnaBoard);
    }
}










