package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.ReviewBoardDTO;
import com.connectravel.entity.Reservation;
import com.connectravel.entity.ReviewBoard;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ReservationRepository;
import javassist.NotFoundException;
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

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;  // 회원 정보를 가져오기 위한 의존성 주입

    @Test
    @DisplayName("숙소에 대한 리뷰를 작성하는 테스트")
    public void testRegister() throws NotFoundException {
        // 기존의 예약 정보 가져오기
        Long rvno = 7L;  // 가정된 예약 ID
        Reservation reservation = reservationRepository.findById(rvno)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        // 회원 이메일 검증
        String writerEmail = "testmember@example.com";  // 데이터베이스에 존재하는 회원 이메일
        boolean memberExists = memberRepository.findByEmail(writerEmail).isPresent();
        if (!memberExists) {
            throw new NotFoundException("Member not found");
        }

        // DTO 생성
        ReviewBoardDTO dto = ReviewBoardDTO.builder()
                .grade(5d)
                .content("리뷰게시판 테스트...")
                .rno(reservation.getRoom().getRno())  // 예약 정보에서 방 번호를 가져옵니다.
                .ano(49L)  // 숙소 번호는 예제로 1L로 설정했습니다.
                .rvno(rvno)  // 예약 ID를 설정합니다.
                .writerEmail(writerEmail)
                .build();

        // 후기 등록 테스트
        Long rbno = reviewBoardService.createReview(dto);

        // 테스트 검증
        assertNotNull(rbno, "The review must be registered and return a review board number");
    }

    @Test
    @DisplayName("숙소에 대한 페이징된 리뷰 목록 테스트")
    void getPaginatedReviewsByAccommodation() {
        // 존재하는 숙소 번호
        Long ano = 49L;
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<ReviewBoardDTO, ReviewBoard> paginatedResult =
                reviewBoardService.getPaginatedReviewsByAccommodation(ano, pageRequestDTO);

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
    void getReviewByRbno() {
        // 존재하는 리뷰 게시물 번호
        Long rbno = 3L;

        // 테스트할 메소드 호출
        ReviewBoardDTO foundReview = reviewBoardService.getReviewByRbno(rbno);

        // 검증
        assertNotNull(foundReview);
        assertEquals(rbno, foundReview.getRbno());

        // 로그 내용 작성
        log.info("검색된 리뷰 게시물 번호: {}", foundReview.getRbno());
        log.info("리뷰 내용: {}", foundReview.getContent());
        log.info("리뷰 작성자 이메일: {}", foundReview.getWriterEmail());
        log.info("리뷰 작성자 이름: {}", foundReview.getWriterName());
        log.info("리뷰 등록일: {}", foundReview.getRegDate());
        log.info("리뷰 수정일: {}", foundReview.getModDate());
    }


    @Test
    @DisplayName("리뷰 수정 테스트")
    void updateReview() {
        // 존재하는 리뷰 게시물 번호
        Long rbno = 3L;

        // 테스트를 위해 불러온 리뷰 정보
        ReviewBoardDTO reviewToBeUpdated = reviewBoardService.getReviewByRbno(rbno);

        // 수정할 내용
        String updatedContent = "업데이트된 리뷰 내용입니다.";

        // 리뷰 내용 수정
        reviewToBeUpdated.setContent(updatedContent);
        reviewBoardService.updateReview(reviewToBeUpdated);

        // 수정된 리뷰 정보 검색
        ReviewBoardDTO updatedReview = reviewBoardService.getReviewByRbno(rbno);

        // 로그 내용 작성
        log.info("리뷰 게시물 번호: {}", updatedReview.getRbno());
        log.info("수정된 리뷰 내용: {}", updatedReview.getContent());

        // 검증
        assertNotNull(updatedReview);
        assertEquals(updatedContent, updatedReview.getContent());
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    void deleteReview() {
        // 존재하는 리뷰 게시물 번호
        Long rbno = 3L; //

        // 삭제 전 리뷰가 존재하는지 확인
        ReviewBoardDTO existingReview = reviewBoardService.getReviewByRbno(rbno);
        assertNotNull(existingReview, "삭제 전 리뷰가 존재해야 합니다.");
        log.info("삭제 전 리뷰 확인 - 게시물 번호: {}, 내용: {}", existingReview.getRbno(), existingReview.getContent());

        // 테스트할 메소드를 호출해 삭제
        reviewBoardService.deleteReview(rbno);
        log.info("리뷰 삭제 수행 - 게시물 번호: {}", rbno);

        // 삭제 후 리뷰가 존재하지 않는지 확인
        Exception exception = assertThrows(
                NotFoundException.class,
                () -> reviewBoardService.getReviewByRbno(rbno),
                "NotFoundException이 발생해야 합니다."
        );
        log.info("삭제 후 리뷰 존재 여부 확인 - 예외 메시지: {}", exception.getMessage());

        // 예외 메시지 확인
        assertTrue(exception.getMessage().contains("리뷰를 찾을 수 없습니다."), "예외 메시지에 '리뷰를 찾을 수 없습니다'가 포함되어야 합니다.");
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
