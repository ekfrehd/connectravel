package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.ReviewBoardDTO;
import com.connectravel.entity.ReviewBoard;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReviewBoardServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ReviewBoardServiceTest.class);

    @Autowired
    private ReviewBoardService reviewBoardService;


    private ReviewBoardDTO reviewBoardDTO;

    @BeforeEach
    void setUp() {
        // reviewBoardDTO 설정을 위한 코드
    }

    @Test
    @DisplayName("숙소에 대한 페이징된 리뷰 목록 테스트")
    void getPaginatedReviewsByAccommodation() {
        // DB에 있는 숙소 번호와 pageRequestDTO
        Long accommodationNumber = 49L;
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<ReviewBoardDTO, ReviewBoard> paginatedResult =
                reviewBoardService.getPaginatedReviewsByAccommodation(accommodationNumber, pageRequestDTO);

        // 검증
        assertNotNull(paginatedResult);
        assertThat(paginatedResult.getDtoList()).isNotEmpty();

        // 로그 내용 작성
        log.debug("페이지 정보: 현재 페이지 {} / 전체 페이지 {}", paginatedResult.getPage(), paginatedResult.getTotalPage());
        log.debug("페이지당 리뷰 수: {}", paginatedResult.getSize());
        log.debug("시작 페이지 번호: {}", paginatedResult.getStart());
        log.debug("끝 페이지 번호: {}", paginatedResult.getEnd());
        log.debug("이전 페이지 존재 여부: {}", paginatedResult.isPrev());
        log.debug("다음 페이지 존재 여부: {}", paginatedResult.isNext());
        log.debug("페이지 번호 목록: {}", paginatedResult.getPageList());
        log.debug("현재 페이지 리뷰 목록:");
        paginatedResult.getDtoList().forEach(review ->
                log.debug("리뷰 ID: {}, 내용: {}", review.getRbno(), review.getContent())
        );
    }

    @Test
    @DisplayName("특정 리뷰 게시물 번호로 리뷰 검색 테스트")
    void getReviewByBno() {
        // 가짜 게시물 번호
        Long boardNumber = 1L;

        // 테스트할 메소드 호출
        ReviewBoardDTO foundReview = reviewBoardService.getReviewByBno(boardNumber);

        // 검증
        assertNotNull(foundReview);
        assertEquals(foundReview.getRbno(), boardNumber);
    }

    @Test
    @DisplayName("리뷰 업데이트 테스트")
    void updateReview() {
        // 새로운 내용 설정
        reviewBoardDTO.setContent("업데이트된 내용");

        // 테스트할 메소드 호출
        reviewBoardService.updateReview(reviewBoardDTO);

        // 업데이트된 리뷰 검색
        ReviewBoardDTO updatedReview = reviewBoardService.getReviewByBno(reviewBoardDTO.getRbno());

        // 검증
        assertEquals("업데이트된 내용", updatedReview.getContent());
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    void deleteReview() {
        // 가짜 게시물 번호
        Long boardNumber = 1L;

        // 테스트할 메소드를 호출해 삭제
        reviewBoardService.deleteReview(boardNumber);

        // 삭제 확인
        NotFoundException thrown = assertThrows(
                NotFoundException.class,
                () -> reviewBoardService.getReviewByBno(boardNumber),
                "getReviewByBno() 호출시 예외가 발생해야 합니다."
        );

        assertTrue(thrown.getMessage().contains("리뷰를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("특정 리뷰의 이미지 목록 테스트")
    void listReviewImages() {
        // 가짜 게시물 번호
        Long boardNumber = 1L;

        // 테스트할 메소드 호출
        List<ImgDTO> images = reviewBoardService.listReviewImages(boardNumber);

        // 검증
        assertNotNull(images);
        assertThat(images).isNotEmpty();
    }
}
