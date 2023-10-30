package com.connectravel.repository;

import com.connectravel.entity.Member;
import com.connectravel.entity.TourBoard;
import com.connectravel.entity.TourBoardReview;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class TourBoardReviewRepositoryTests {

    @Autowired
    private TourBoardReviewRepository tourBoardReviewRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TourBoardRepository tourBoardRepository;

    @Test // 게시글만 추가 테스트
    public void testTourBoarReviewInsert () {
        Optional<TourBoard> resultTourBoard = tourBoardRepository.findById (4L);
        TourBoard tourBoard = resultTourBoard.orElseThrow (() -> new NoSuchElementException ("게시글이 존재하지 않습니다."));
        Member member = memberRepository.findByEmail ("1111@naver.com");

        IntStream.rangeClosed (1, 10).forEach (i -> {//1부터 10까지 생성
            TourBoardReview tourBoardReview = TourBoardReview.builder ()
                    .content ("추천합니다.")
                    .recommend (5)
                    .tourBoard (tourBoard)
                    .member (member)
                    .build ();

            TourBoardReview result = tourBoardReviewRepository.save (tourBoardReview);
            System.out.println (result);
        });
    }

    @Test // 게시글 조회 테스트
    public void testTourBoardReviewReadOne () {
        Long tbno = 2L; // 존재하는 게시글 입력
        tourBoardRepository.findById (tbno);
        System.out.println ( tourBoardRepository.findById (tbno));
    }

    @Test // 게시글 수정 테스트
    public void testTourBoardReviewUpdate () {
        Long trbno = 2L;//n번째 게시글, 존재하는 게시글 입력
        //Optional 객체는 null 에러 방지
        Optional<TourBoard> result = tourBoardRepository.findById (trbno);
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
    public void testTourBoardReviewDelete () {
        Long tbrno = 1L;
        tourBoardReviewRepository.deleteById (tbrno);
    }
}










