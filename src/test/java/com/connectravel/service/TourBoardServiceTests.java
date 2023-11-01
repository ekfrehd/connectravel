package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.TourBoardDTO;
import com.connectravel.entity.QnaBoard;
import com.connectravel.entity.TourBoard;
import com.connectravel.entity.TourBoardImg;
import com.connectravel.repository.TourBaordImgRepository;
import com.connectravel.repository.TourBoardRepository;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class TourBoardServiceTests {

    @Autowired
    TourBoardRepository tourBoardRepository;
    @Autowired
    private TourBoardService tourBoardService;
    @Autowired
    private ImgService imgService;
    @Autowired
    private TourBaordImgRepository tourBaordImgRepository;


    @Test // 가이드 글 추가 테스트
    public void testRegisterTourBoard() {
        // 테스트에 사용할 데이터 생성
        TourBoardDTO tourBoardDTO = new TourBoardDTO();
        tourBoardDTO.setTitle("가이드 더미 데이터");
        tourBoardDTO.setContent("가이드 더미 데이터");
        tourBoardDTO.setRegion("서울");
        tourBoardDTO.setCategory("명소");
        tourBoardDTO.setPostal(10000);
        tourBoardDTO.setAddress("수원");

        // register 메소드 호출
        Long savedTbno = tourBoardService.register(tourBoardDTO);

        // 예상한 결과를 확인하는 단언문(assertion) 추가
        assertNotNull(savedTbno, "저장된 TourBoard의 tbno는 null이 아니어야 합니다.");
        // 그 외에 필요한 단언문(assertion)을 추가하세요
    }

    @Test // 가이드 글, 이미지 추가  테스트
    public void testRegisterTourBoard_Ima() {
        // 테스트에 사용할 데이터 생성
        TourBoardDTO tourBoardDTO = new TourBoardDTO();
        tourBoardDTO.setTitle("가이드 더미 데이터");
        tourBoardDTO.setContent("가이드 더미 데이터");
        tourBoardDTO.setRegion("서울");
        tourBoardDTO.setCategory("명소");
        tourBoardDTO.setPostal(10000);
        tourBoardDTO.setAddress("수원");

        // 가짜 이미지 파일 생성
        byte[] fileContent = "Test image content".getBytes(); // 가짜 이미지 파일 데이터
        MockMultipartFile mockFile = new MockMultipartFile("file", "test-image.jpg", "image/jpeg", fileContent);

        // register 메소드 호출
        Long savedTbno = tourBoardService.register(tourBoardDTO);

        // TourBoardRegister 메소드 호출
        imgService.TourBoardRegister(mockFile, savedTbno);
    }

    @Test // 게시글 조회 테스트
    public void testRead() {
        Long tbno = 5L;

        // read 메소드 호출
        TourBoardDTO result = tourBoardService.read(tbno);
        System.out.println(result);
    }

    @Test
    public void testModify() {
        // DTO 생성 및 변경할 내용 설정
        TourBoardDTO dto = new TourBoardDTO();
        dto.setTbno(5L);
        dto.setTitle("New Title");
        dto.setContent("New Content");

        // modify 메소드 호출
        tourBoardService.modify(dto);
    }

    @Test
    public void testRemove() {
        Long tbno = 7L;

        // read 메소드 호출
        tourBoardService.remove(tbno);
    }
}









