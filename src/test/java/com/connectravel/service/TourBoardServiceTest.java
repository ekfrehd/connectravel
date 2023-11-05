package com.connectravel.service;

import com.connectravel.dto.TourBoardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TourBoardServiceTest {

    @Autowired
    private TourBoardService tourBoardService;

    private TourBoardDTO testTourBoardDTO;

    @BeforeEach
    void setUp() {
        // 테스트를 위한 TourBoardDTO 객체 생성
        testTourBoardDTO = TourBoardDTO.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .region("테스트 지역")
                .category("테스트 카테고리")
                .address("테스트 주소")
                .postal(12345)
                .build();
    }

    @Test
    @DisplayName("투어 게시물 생성 테스트")
    void createTourBoardTest() {
        // TourBoardDTO 객체를 이용해 TourBoard 생성 테스트
        Long tbno = tourBoardService.createTourBoard(testTourBoardDTO);
        assertNotNull(tbno, "TourBoard 생성 후 ID는 null이 아니어야 합니다.");
    }

    @Test
    @DisplayName("투어 게시물 조회 테스트")
    void getTourBoardTest() {
        // TourBoardDTO 객체를 이용해 TourBoard 생성 후 조회 테스트
        Long tbno = tourBoardService.createTourBoard(testTourBoardDTO);
        TourBoardDTO foundTourBoardDTO = tourBoardService.getTourBoard(tbno);
        assertNotNull(foundTourBoardDTO, "생성된 TourBoard는 조회가 가능해야 합니다.");
        assertEquals(testTourBoardDTO.getTitle(), foundTourBoardDTO.getTitle(), "제목이 일치해야 합니다.");
    }

    @Test
    @DisplayName("투어 게시물 업데이트 테스트")
    void updateTourBoardTest() {
        // TourBoardDTO 객체를 이용해 TourBoard 생성 후 업데이트 테스트
        Long tbno = tourBoardService.createTourBoard(testTourBoardDTO);
        testTourBoardDTO.setTbno(tbno);
        testTourBoardDTO.setTitle("업데이트된 제목");

        tourBoardService.updateTourBoard(testTourBoardDTO);

        TourBoardDTO updatedTourBoardDTO = tourBoardService.getTourBoard(tbno);
        assertNotNull(updatedTourBoardDTO, "업데이트된 TourBoard는 존재해야 합니다.");
        assertEquals("업데이트된 제목", updatedTourBoardDTO.getTitle(), "TourBoard 제목이 업데이트되어야 합니다.");
    }

    @Test
    @DisplayName("투어 게시물 삭제 테스트")
    void deleteTourBoardTest() {
        // TourBoardDTO 객체를 이용해 TourBoard 생성 후 삭제 테스트
        Long tbno = tourBoardService.createTourBoard(testTourBoardDTO);
        tourBoardService.deleteTourBoard(tbno);

        TourBoardDTO deletedTourBoardDTO = tourBoardService.getTourBoard(tbno);
        assertNull(deletedTourBoardDTO, "삭제된 TourBoard는 조회될 때 null이 반환되어야 합니다.");
    }

}
