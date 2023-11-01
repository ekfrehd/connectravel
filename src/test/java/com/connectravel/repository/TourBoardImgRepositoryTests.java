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

    @Test
    public void testTourBoardImgInsert() {
        Optional<TourBoard> optionalTbno = tourBoardRepository.findById(21L);
        TourBoard tbno = optionalTbno.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다."));

        TourBoardImg tourBoardImg = TourBoardImg.builder().imgFile("sample.jpg").tourBoard(tbno).build();

        tourBaordImgRepository.save(tourBoardImg);
        System.out.println(tourBaordImgRepository.save(tourBoardImg));
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

    @Test // 게시글 삭제 테스트, 댓글이 있으면 안지워짐
    public void testTourBoardImgDelete() {
        Long ino = 3L;
        tourBaordImgRepository.deleteById(ino);
    }
}










