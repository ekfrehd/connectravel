package com.connectravel.repository;

import com.connectravel.entity.Member;
import com.connectravel.entity.TourBoard;
import com.connectravel.entity.TourBoardReview;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class TourBoardReviewRepositoryTests {

    @Autowired
    private TourBoardReviewRepository tourBoardReviewRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TourBoardRepository tourBoardRepository;

    @Test // 가이드 리뷰 추가 테스트
    public void testTourBoarReviewInsert() {
        Optional<TourBoard> resultTourBoard = tourBoardRepository.findById(1L);
        TourBoard tourBoard = resultTourBoard.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다."));
        Member member = memberRepository.findByEmail("sample@sample.com");

        IntStream.rangeClosed(1, 5).forEach(i -> { // 1부터 10까지 생성
            TourBoardReview tourBoardReview = TourBoardReview.builder().content("추천합니다.").recommend(i).tourBoard(tourBoard).member(member).build();

            TourBoardReview result = tourBoardReviewRepository.save(tourBoardReview);
            System.out.println(result);
        });
    }

    @Test // 가이드 리뷰 조회 테스트
    @Transactional
    public void testTourBoardReviewReadOne() {

        // tbrno에 해당하는 리뷰 조회
        Long tbrno = 1L;
        Optional<TourBoardReview> tourBoardReviewOptional = tourBoardReviewRepository.findById(tbrno);
        System.out.println(tourBoardReviewOptional);
    }

    @Test // 가이드 리뷰 수정 테스트
    public void testTourBoardReviewUpdate() {
        Long tbrno = 1L;

        Optional<TourBoardReview> tourBoardReviewOptional = tourBoardReviewRepository.findById(tbrno); // 리뷰 조회
        assertTrue(tourBoardReviewOptional.isPresent(), "게시글이 존재하지 않습니다."); // 게시글 존재 확인

        TourBoardReview tourBoardReview = tourBoardReviewOptional.get(); // 리뷰가 존재한다면 가져옴

        tourBoardReview.changeContent("리뷰 내용 수정"); // 리뷰 내용 및 등급 수정
        tourBoardReview.changeGrade(5D);
        tourBoardReviewRepository.save(tourBoardReview); // 수정 내용을 데이터베이스에 반영
    }

    @Test // 가이드 리뷰 삭제 테스트, 댓글이 있으면 안지워짐
    public void testTourBoardReviewDelete() {
        Long tbrno = 11L;
        tourBoardReviewRepository.deleteById(tbrno);
    }
}










