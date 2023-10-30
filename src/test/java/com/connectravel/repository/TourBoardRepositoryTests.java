package com.connectravel.repository;

import com.connectravel.dto.MemberFormDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoard;
import com.connectravel.entity.TourBoard;
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
public class TourBoardRepositoryTests {

    @Autowired
    private TourBoardRepository tourBoardRepository;

    @Test // 게시글만 추가 테스트
    public void testTourBoardInsert () {
        IntStream.rangeClosed (1, 10).forEach (i -> {//1부터 10까지 생성
            TourBoard tourBoard = TourBoard.builder ()
                    .title ("가이드 더미 데이터" + i)
                    .content ("가이드 더미 데이터" + i)
                    .region ("서울")
                    .category ("명소")
                    .postal (10000)
                    .address ("수원")
                    .build ();
            TourBoard result = tourBoardRepository.save (tourBoard);
            System.out.println ("NO: " + result.getTbno ());
        });
    }

    @Test // 게시글 조회 테스트
    public void testTourBoardReadOne () {
        Long tbno = 2L; // 존재하는 게시글 입력
        tourBoardRepository.findById (tbno);
        System.out.println ( tourBoardRepository.findById (tbno));
    }

    @Test // 게시글 수정 테스트
    public void testTourBoardUpdate () {
        Long tbno = 2L;//n번째 게시글, 존재하는 게시글 입력
        //Optional 객체는 null 에러 방지
        Optional<TourBoard> result = tourBoardRepository.findById (tbno);
        //result가 null일 경우 board에 담는다.
        TourBoard tourBoard = result.orElseThrow (() -> new NoSuchElementException ("게시글이 존재하지 않습니다."));
        //board 클래스의 change 메서드 실행
        tourBoard.changeTitle ("게시글 제목 수정");
        tourBoard.changeContent ("게시글 내용 수정");
        //업데이트 내용 DB 저장
        System.out.println (tourBoard);
        tourBoardRepository.save (tourBoard);
    }

    @Test // 게시글 삭제 테스트, 댓글이 있으면 안지워짐
    public void testTourBoardDelete () {
        Long tbno = 2L;
        tourBoardRepository.deleteById (tbno);
    }
}










