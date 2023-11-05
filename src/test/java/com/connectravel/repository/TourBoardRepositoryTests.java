package com.connectravel.repository;

import com.connectravel.entity.TourBoard;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class TourBoardRepositoryTests {

    @Autowired
    private TourBoardRepository tourBoardRepository;

    @Test // 게시글만 추가 테스트
    public void testTourBoardInsert() {
        IntStream.rangeClosed(1, 10).forEach(i -> { // 1부터 10까지 생성
            TourBoard tourBoard = TourBoard.builder()
                    .title("가이드 더미 데이터" + i)
                    .content("가이드 더미 데이터" + i)
                    .region("서울")
                    .category("명소")
                    .postal(10000)
                    .address("수원")
                    .build();

            TourBoard result = tourBoardRepository.save(tourBoard); // DB 반영
            System.out.println("NO: " + result.getTbno());
        });
    }

    @Test // 게시글 전체 조회 테스트
    public void testTourBoardReadOne1 () {
        Long tbno = 5L; // 존재하는 게시글의 번호
        Object result = tourBoardRepository.getBoardByBno(tbno); // tbno가 일치하는 가이드 게시글과 리뷰 개수를 가져온다.
        if (result != null) { // 결과가 Object 타입이므로 해당 객체를 적절한 타입으로 캐스팅하여 사용
            Object[] row = (Object[]) result;
            TourBoard tourBoard = (TourBoard) row[0]; // 게시글 정보
            Long replyCount= (Long) row[1]; // 게시글 댓글 정보

            System.out.println("게시글 정보: " + tourBoard);
            System.out.println("게시글 리뷰 수: " + replyCount);
        } else {
            System.out.println("해당하는 결과가 없습니다.");
        }
    }

    @Test // 게시글 수정 테스트
    public void testTourBoardUpdate() {
        Long tbno = 5L; // n번째 게시글, 존재하는 게시글 입력

        Optional<TourBoard> result = tourBoardRepository.findById(tbno); // Optional 객체는 null 에러 방지
        System.out.println("기존 게시물 정보 : " + result);
        TourBoard tourBoard = result.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다.")); // 예외처리
        tourBoard.changeTitle("게시글 제목 수정2"); // board 클래스의 change 메서드 실행
        tourBoard.changeContent("게시글 내용 수정2");
        tourBoard.changeAddress("병점3로 111...");
        tourBoard.changeCategory("음식");
        tourBoard.changeRegion("병점");
        tourBoard.changePostal(11111);

        tourBoardRepository.save(tourBoard); // DB 반영

        System.out.println("수정 게시물 정보 : " + tourBoard);
    }

    @Test // 게시글 삭제 테스트, 댓글이 있으면 안지워짐
    public void testTourBoardDelete() {
        Long tbno = 11L;

        Optional<TourBoard> optionalTourBoard = tourBoardRepository.findById(tbno); // 게시글 조회
        TourBoard tourBoard = optionalTourBoard.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다.")); // 예외 처리

        tourBoardRepository.deleteById(tbno); // 게시글이 존재하면 삭제
    }
}




