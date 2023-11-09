package com.connectravel.repository;

import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest//테스트 클래스
@Log4j2//로그 사용
public class SearchBoardRepositoryTests {

    @Autowired
    TourBoardRepository tourBoardRepository;

    @Test
    public void searchTourBoard_ReturnsExpectedResults() {
        // 테스트용 데이터
        String[] type = {"c"};
        String keyword = "keyword";
        String category = "category";
        String region = "region";
        String address = "address";
        Pageable pageable = PageRequest.of(1, 10);

        // 예상 결과 크기
        int expectedPageSize = 10;

        // 메서드 호출
        Page<Object[]> result = tourBoardRepository.searchTourBoard(type, keyword, category, region, pageable, address);
        System.out.println(result);
    }
}