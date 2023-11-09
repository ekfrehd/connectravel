package com.connectravel.service;

import com.connectravel.entity.ReviewBoardImg;
import com.connectravel.manager.FileManager;
import com.connectravel.repository.ReviewBoardImgRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ImgServiceTest {
    private static final Logger log = LoggerFactory.getLogger(ImgServiceTest.class);

    @MockBean
    private ReviewBoardImgRepository mockReviewBoardImgRepository;

    @MockBean
    private FileManager mockFileManager;

    @Autowired
    private ImgService imgService;

    @Test
    @DisplayName("리뷰 게시물에 이미지 추가 테스트")
    void testAddReviewBoardImg() throws IOException {
        // 준비: 테스트용 파일 데이터와 리뷰 게시판 번호 설정
        Long rbno = 1L;
        MultipartFile mockMultipartFile = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image content".getBytes());

        // mockFileManager 행동 정의: UUID 생성과 파일 추가를 모킹 처리
        when(mockFileManager.UUIDMaker(anyString())).thenReturn("unique-file-name.jpg");
        when(mockFileManager.add(any(MultipartFile.class), anyString())).thenReturn(true);

        // 실행: 이미지 서비스를 통해 리뷰 게시물에 이미지 추가 처리
        imgService.addReviewBoardImg(mockMultipartFile, rbno);

        // 검증: 리뷰 게시판 이미지 리포지토리에 save 메소드가 호출되었는지 확인
        verify(mockReviewBoardImgRepository, times(1)).save(any(ReviewBoardImg.class));

        // 로그 기록: 이미지 추가 작업이 성공적으로 수행되었음을 로그로 기록
        log.debug("이미지 '{}'가 리뷰 게시판 ID: {}에 추가되었습니다.", "unique-file-name.jpg", rbno);
    }

}
