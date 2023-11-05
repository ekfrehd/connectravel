package com.connectravel.service;

import com.connectravel.dto.TourBoardDTO;
import com.connectravel.entity.TourBoard;
import com.connectravel.repository.TourBaordImgRepository;
import com.connectravel.repository.TourBoardRepository;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

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

        TourBoardDTO tourBoardDTO = new TourBoardDTO(); // 테스트에 사용할 데이터 생성
        tourBoardDTO.setTitle("가이드 더미 데이터");
        tourBoardDTO.setContent("가이드 더미 데이터");
        tourBoardDTO.setRegion("서울");
        tourBoardDTO.setCategory("명소");
        tourBoardDTO.setPostal(10000);
        tourBoardDTO.setAddress("수원");

        Long savedTbno = tourBoardService.register(tourBoardDTO); // register 메소드 호출

        // 예상한 결과를 확인하는 단언문(assertion) 추가
        assertNotNull(savedTbno, "저장된 TourBoard의 tbno는 null이 아니어야 합니다.");
    }

    @Test // 가이드 글, 이미지 추가  테스트
    public void testRegisterTourBoard_Ima() {

        TourBoardDTO tourBoardDTO = new TourBoardDTO(); // 테스트에 사용할 데이터 생성
        tourBoardDTO.setTitle("가이드 더미 데이터");
        tourBoardDTO.setContent("가이드 더미 데이터");
        tourBoardDTO.setRegion("서울");
        tourBoardDTO.setCategory("명소");
        tourBoardDTO.setPostal(10000);
        tourBoardDTO.setAddress("수원");

        // 가짜 이미지 파일 생성
        byte[] fileContent = "Test image content".getBytes(); // 가짜 이미지 파일 데이터
        MockMultipartFile mockFile = new MockMultipartFile("file", "test-image.jpg", "image/jpeg", fileContent);

        Long savedTbno = tourBoardService.register(tourBoardDTO); // register 메소드 호출

        imgService.TourBoardRegister(mockFile, savedTbno); // TourBoardRegister 메소드 호출
    }

    @Test // 게시글 조회 테스트
    public void testRead() {
        Long tbno = 5L; // 가이드 게시글 번호

        TourBoardDTO result = tourBoardService.read(tbno); // read 메소드 호출
        System.out.println("조회한 게시글 : " + result);
    }

    @Test
    public void testModify() {
        Long tbno = 5L; // 수정할 게시글 번호

        TourBoardDTO dto = new TourBoardDTO(); // DTO 생성 및 변경할 내용 설정
        dto.setTbno(tbno);
        dto.setTitle("New Title");
        dto.setContent("New Content");

        tourBoardService.modify(dto); // modify 메소드 호출

        Optional<TourBoard> optionalTourBoard = tourBoardRepository.findById(tbno); // 게시글 정보 조회
        TourBoard tourBoard = optionalTourBoard.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다.")); // 예외 처리

        System.out.println("수정한 게시글 : " + tourBoard);
    }

    @Test
    public void testRemove() {
        Long tbno = 7L; // 삭제할 가이드 게시글 번호

        tourBoardService.remove(tbno); // remove 메소드 호출
    }
}









