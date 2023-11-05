package com.connectravel.repository;

import com.connectravel.entity.TourBoard;
import com.connectravel.entity.TourBoardImg;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class TourBoardImgRepositoryTests {

    @Autowired
    private TourBaordImgRepository tourBaordImgRepository;
    @Autowired
    private TourBoardRepository tourBoardRepository;

    @Test // 가이드 게시글 이미지 입력
    public void testTourBoardImgInsert() {
        Optional<TourBoard> optionalTbno = tourBoardRepository.findById(1L); // 해당 게시글 조회
        TourBoard tbno = optionalTbno.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다.")); // 예외 처리

        TourBoardImg tourBoardImg = TourBoardImg.builder() // 게시글 이미지 정보 입력
                .imgFile("sample.jpg")
                .tourBoard(tbno)
                .build();

        tourBaordImgRepository.save(tourBoardImg); // DB 반영
        System.out.println("게시글 번호 : " + tbno.getTbno());
        System.out.println("입력된 이미지 정보 : " + tourBoardImg);
    }

    @Test // 가이드 게시글 이미지 삭제
    public void testTourBoardImgDelete() {
        Long ino = 5L;

        Optional<TourBoardImg> optionalTourBoardImg = tourBaordImgRepository.findById(ino); // 게시글 이미지 조회
        TourBoardImg tourBoardImg = optionalTourBoardImg.orElseThrow(() -> new NoSuchElementException("이미지가 존재하지 않습니다.")); // 예외 처리

        tourBaordImgRepository.deleteById(tourBoardImg.getIno()); // 게시글 이미지가 존재하면 삭제 처리
    }
}
/*@Test
    public void testInsertWithImages() {
        TourBoard tourBoard = TourBoard.builder()
                .title("여행 게시판 데이터")
                .content("여행 게시판 데이터")
                .region("부산")
                .category("풍경")
                .postal(20000)
                .address("부산")
                .build();

        // 이미지 1 추가
        tourBoard.addImage(10L, "file1.jpg");

        // 이미지 2 추가
        tourBoard.addImage(20L, "file2.jpg");

        // 여행게시판과 이미지들을 저장
        tourBoardRepository.save(tourBoard);
    }*/








