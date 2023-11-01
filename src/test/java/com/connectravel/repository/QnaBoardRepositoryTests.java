package com.connectravel.repository;

import com.connectravel.dto.MemberFormDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoard;
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

    @Test // 게시글만 추가 테스트
    public void testQnaBoardInsert() {
        IntStream.rangeClosed(1, 10).forEach(i -> { // 1부터 10까지 생성
            QnaBoard qnaBoard = QnaBoard.builder().title("QnA 게시글 더미데이터" + i).content("QnA 게시글 내용 더미데이터" + i).build(); // 빌더 패턴 사용 Qna 게시글 데이터 입력
            QnaBoard result = qnaBoardRepository.save(qnaBoard); // Qna 게시글 DB 저장
            System.out.println("BNO: " + result.getBno());
        });
    }

    private Member createMember() {
        MemberFormDTO memberFormDTO = new MemberFormDTO();
        memberFormDTO.setName("더미봇");
        memberFormDTO.setEmail("sample@sample.com");
        memberFormDTO.setNickName("더미봇");
        memberFormDTO.setTel("010-0000-0000");
        memberFormDTO.setPassword("1234");
        return Member.createMember(memberFormDTO);
    }

    @Test //특정 회원의 게시글 추가 테스트
    public void testQnaBoardInsertAddMember() {
        Member member = createMember(); // 멤버 생성
        memberRepository.save(member); // 멤버  DB 저장

        IntStream.rangeClosed(1, 10).forEach(i -> {//1부터 10까지 생성
            QnaBoard qnaBoard = QnaBoard.builder().title("QnA 게시글 더미데이터" + i).content("QnA 게시글 내용 더미데이터" + i).member(member).build(); // 빌더 패턴 사용 Qna 게시글 데이터 입력
            QnaBoard result = qnaBoardRepository.save(qnaBoard); // Qna 게시글 DB 저장
            System.out.println("BNO: " + result.getBno());
        });
    }

    @Test // 게시글 조회 테스트
    public void testQnaBoardReadOne() {
        Long bno = 10L; // 존재하는 게시글 입력
        qnaBoardRepository.findById(bno); // 해당 번호의 게시글 데이터를 조회
        System.out.println(qnaBoardRepository.findById(bno));
    }

    @Test // 게시글 전체 조회 테스트
    public void testQnaBoardReadOne1() {
        Long bno = 1L; // 존재하는 게시글의 번호
        Object result = qnaBoardRepository.getBoardByBno(bno);
        if (result != null) { // 결과가 Object 타입이므로 해당 객체를 적절한 타입으로 캐스팅하여 사용
            Object[] row = (Object[]) result;
            QnaBoard qnaBoard = (QnaBoard) row[0]; // 게시글 정보
            Member member = (Member) row[1]; // 회원 정보
            Long replyCount = (Long) row[2]; // 게시글 댓글 정보

            System.out.println("게시글 정보: " + qnaBoard);
            System.out.println("회원 정보: " + member);
            System.out.println("게시글 댓글 수: " + replyCount);
        }
        else {
            System.out.println("해당하는 결과가 없습니다.");
        }
    }

    @Test // 게시글 수정 테스트
    public void testQnaBoardUpdate() {
        Long bno = 9L; // 존재하는 게시글 입력
        Optional<QnaBoard> result = qnaBoardRepository.findById(bno); // Optional 객체는 null 에러 방지, 해당 번호의 게시글 조회
        //System.out.println ("기존 게시글 데이터 : " + result);
        QnaBoard qnaBoard = result.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다.")); // result가 null일 경우 board에 담는다.
        qnaBoard.changeTitle("게시글 제목 수정"); // board 클래스의 change 메서드 실행
        qnaBoard.changeContent("게시글 내용 수정"); // board 클래스의 change 메서드 실행
        qnaBoardRepository.save(qnaBoard); // 업데이트 내용 DB 저장
        //System.out.println ("수정된 게시글 데이터 : " + qnaBoard);
    }

    @Test // 게시글 삭제 테스트, 댓글이 있으면 안지워짐
    public void testQnaBoardDelete() {
        Long bno = 11L; // 존재하는 게시글 입력
        qnaBoardRepository.deleteById(bno); // 해당 번호의 게시글 데이터 삭제
    }
}










