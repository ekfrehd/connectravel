package com.connectravel.service;

import com.connectravel.dto.TourBoardReviewReplyDTO;
import com.connectravel.repository.TourBoardReviewReplyRepository;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class TourBoardReviewReplyServiceTests {

    @Autowired
    private TourBoardReviewReplyService tourBoardReviewReplyService;

    @Autowired
    private TourBoardReviewReplyRepository tourBoardReviewReplyRepository;

    @Test
    void testRegister() {
        // Given
        TourBoardReviewReplyDTO tourBoardReviewReplyDTO = new TourBoardReviewReplyDTO();
        tourBoardReviewReplyDTO.setTbrno(6L);
        tourBoardReviewReplyDTO.setText("답변 드립니다.");

        Long result = tourBoardReviewReplyService.register(tourBoardReviewReplyDTO);
        System.out.println(result);
    }

    @Test // 댓글 수정 테스트
    public void testModify() {
        Long tbrrno = 2L;

        TourBoardReviewReplyDTO tourBoardReviewReplyDTO = new TourBoardReviewReplyDTO();
        tourBoardReviewReplyDTO.setTbrrno(tbrrno);
        tourBoardReviewReplyDTO.setText("답변 수정드립니다.");
        tourBoardReviewReplyDTO.setReplyer("더미봇");

        tourBoardReviewReplyService.modify(tourBoardReviewReplyDTO); // 게시글 수정 메서드
    }

    @Test // 댓글 삭제 테스트
    public void testRemove() {
        Long tbrrno = 2L;

        TourBoardReviewReplyDTO tourBoardReviewReplyDTO = new TourBoardReviewReplyDTO();
        tourBoardReviewReplyDTO.setTbrno(tbrrno);

        tourBoardReviewReplyService.remove(tourBoardReviewReplyDTO.getTbrno()); // 게시글 삭제 메서드
    }
}









