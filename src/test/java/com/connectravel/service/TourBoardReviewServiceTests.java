package com.connectravel.service;

import com.connectravel.dto.TourBoardReivewDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.TourBoard;
import com.connectravel.entity.TourBoardReview;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.TourBoardRepository;
import com.connectravel.repository.TourBoardReviewRepository;
import groovy.util.logging.Log4j2;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class TourBoardReviewServiceTests {

    @Autowired
    private TourBoardReviewService tourBoardReviewService;
    @Autowired
    private TourBoardReviewRepository tourBoardReviewRepository;
    @Autowired
    private TourBoardRepository tourBoardRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test // 가이드 댓글 추가 테스트
    public void testRegister() throws NotFoundException {

        TourBoardReivewDTO dto = TourBoardReivewDTO.builder()
                .tbno(5L)
                .grade(4D)
                .content("유익해요.")
                .build();

        Optional<Member> memberOptional = Optional.ofNullable(memberRepository.findByEmail("sample@sample.com"));
        if (!memberOptional.isPresent()) { throw new NotFoundException("Member not found");}
        Member member = memberOptional.get();

        Optional<TourBoard> tourBoardOptional = tourBoardRepository.findById(dto.getTbno());
        if (!tourBoardOptional.isPresent()) {throw new NotFoundException("TourBoard not found");}
        TourBoard tourBoard = tourBoardOptional.get();

        int currentCount = tourBoard.getReviewCount(); // 현재 리뷰 수
        double currentGrade = tourBoard.getGrade(); // 현재 평점
        double newGrade = dto.getGrade(); // 새 평점
        double avarageGrade = ((currentGrade * currentCount) + newGrade) / (currentCount + 1); // 평균 평점 = 현재 평점 * 현재 리뷰 수 / 현재 리뷰 수 + 1

        tourBoard.setReviewCount(currentCount + 1); // 게시글의 리뷰 수를 올린다.
        tourBoard.setGrade(avarageGrade); // 게시글의 평균 평점 입력
        tourBoardRepository.saveAndFlush(tourBoard); // DB 반영

        TourBoardReview tourBoardReview = tourBoardReviewService.dtoToEntity(dto, tourBoard, member); // 리뷰 정보, 게시글 정보, 회원 정보 입력
        tourBoardReviewRepository.save(tourBoardReview); // DB 반영
    }

    @Test // 댓글 출력
    public void testReadTourBoardReview() {
        Long tbrno = 1L;

        TourBoardReivewDTO result = tourBoardReviewService.get(tbrno);
        System.out.println(result);
    }


    @Test // 댓글 수정
    public void testModifyTourBoardReview() {
        TourBoardReivewDTO reviewDTO = TourBoardReivewDTO.builder()
                .tbno(5L)
                .tbrno(6L)
                .content("댓글 수정 완료")
                .grade(5D)
                .build();

        Optional<TourBoard> optionalTourBoard = tourBoardRepository.findById(reviewDTO.getTbno()); // 리뷰가 속한 여행 게시글 조회
        TourBoard tourBoard = optionalTourBoard.orElseThrow(() -> new NoSuchElementException("여행 게시글이 존재하지 않습니다."));

        Optional<TourBoardReview> optionalTourBoardReview = tourBoardReviewRepository.findById(reviewDTO.getTbrno()); // 기존 리뷰 조회
        TourBoardReview tourBoardReview = optionalTourBoardReview.orElseThrow(() -> new NoSuchElementException("리뷰가 존재하지 않습니다."));

        // 기존 게시글의 리뷰 수, 현재 평점, 기존 댓글의 평점, 새로운 평점 설정
        int currentCount = tourBoard.getReviewCount(); // 현재 게시글 리뷰 수
        double currentGrade = tourBoard.getGrade(); // 현재 평점
        double oldGrade = tourBoardReview.getGrade(); // 기존 리뷰 평점
        double newGrade = reviewDTO.getGrade(); // 수정 리뷰 평점

        double averageGrade = ((currentGrade * currentCount - oldGrade) + newGrade) / currentCount; // 평균 평점 업데이트

        tourBoard.setGrade(averageGrade); // 게시글에 평점 및 리뷰 수 업데이트
        tourBoardRepository.saveAndFlush(tourBoard); // sava and flush, flush = 커밋 또는 롤백하지 않아도 DB 반영

        tourBoardReviewService.modify(reviewDTO); // 댓글 수정 서비스 호출
    }

    @Test
    public void testRemove() throws NotFoundException {
        Long tbno = 5L; // 존재하는 게시글
        Long tbrno = 9L; // 존재하는 게시글의 댓글

        tourBoardReviewService.removeWithReplies(tbrno, tbno); // read 메소드 호출
    }
}









