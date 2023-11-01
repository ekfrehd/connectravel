package com.connectravel.service;

import com.connectravel.dto.TourBoardDTO;
import com.connectravel.dto.TourBoardReivewDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.TourBoard;
import com.connectravel.entity.TourBoardReview;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.TourBaordImgRepository;
import com.connectravel.repository.TourBoardRepository;
import com.connectravel.repository.TourBoardReviewRepository;
import groovy.util.logging.Log4j2;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

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
                .tbno(1L)
                .grade(5D)
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
                .tbrno(1L)
                .content("댓글 수정 완료")
                .grade(1D)
                .build();

        tourBoardReviewService.modify(reviewDTO);
    }

    @Test
    public void testRemove() throws NotFoundException {
        Long tbno = 1L;
        Long tbrno = 6L;

        // read 메소드 호출
        tourBoardReviewService.removeWithReplies(tbrno, tbno);
    }
}









