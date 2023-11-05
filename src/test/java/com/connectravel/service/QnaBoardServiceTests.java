package com.connectravel.service;

import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.QnaBoardDTO;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class QnaBoardServiceTests {

    @Autowired
    private QnaBoardService qnaBoardService;

    @Test // 게시글 추가 테스트
    public void testRegisterQnaBoard() {

        String[] sampleQuestions = { // 질문을 배열로 저장
                "숙박 예약을 어떻게 하나요?",
                "수수료는 얼마인가요?",
                "환불 정책은 어떻게 되나요?",
                "숙박 시설에 대한 자세한 정보를 알 수 있을까요?",
                "커넥트래블의 이용 방법을 설명해 주세요.",
                "어떻게 안전한 결제를 할 수 있을까요?",
                "커넥트래블의 혜택은 무엇인가요?",
                "서비스 이용 시 주의사항을 알려주세요.",
                "커넥트래블의 고객 지원에 대해 설명해 주세요.",
                "추천하는 장소가 있나요?"
        };

        IntStream.rangeClosed(1, 10).forEach(i -> {
        // 테스트 QnaBoardDTO 생성
        QnaBoardDTO qnaBoardDTO = new QnaBoardDTO();
        qnaBoardDTO.setTitle(sampleQuestions[i - 1]);
        qnaBoardDTO.setContent(sampleQuestions[i - 1]);
        qnaBoardDTO.setWriterEmail("sample@sample.com");

        // 게시글 등록 register 메서드 호출
        Long bno = qnaBoardService.register(qnaBoardDTO);

        System.out.println(bno + "번 게시물 등록");
        });
    }

    @Test // 게시글 조회 테스트
    public void testGetQnaBoard() {
        Long bno = 1L; // 테스트 게시글 번호

        QnaBoardDTO result = qnaBoardService.get(bno); // 게시글 조회 get 메서드 호출

        System.out.println(bno + "번 게시글");
        System.out.println("QnaBoard 정보: " + result.toString());
        System.out.println("Member 정보: " + result.getWriterEmail() + ", " + result.getWriterName());
    }

    @Test // 게시글 수정 테스트
    @Transactional
    public void testModifyQnaBoard() {
        Long bno = 1L; // 수정할 게시글 번호

        // 테스트 QnaBoardDTO 생성 (수정할 내용 포함)
        QnaBoardDTO qnaBoardDTO = new QnaBoardDTO();
        qnaBoardDTO.setBno(bno);
        qnaBoardDTO.setTitle("수정된 제목");
        qnaBoardDTO.setContent("수정된 내용");

        qnaBoardService.modify(qnaBoardDTO); // 게시글 수정

        QnaBoardDTO result = qnaBoardService.get(bno); // 수정한 게시글 가져오기
        System.out.println("QnaBoard 정보: " + result.toString());
    }

    @Test // 게시글 삭제 테스트
    public void testRemoveBoard() {
        Long bno = 16L; // 삭제할 게시글 번호

        // 게시글 삭제 get 메서드 호출
        qnaBoardService.removeWithReplies(bno);
    }

    @Test
    @Transactional
    public void testGetList() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(1); // 1 페이지
        pageRequestDTO.setSize(10); // 10개 출력
        pageRequestDTO.setType("t"); // 검색 타입 설정
        pageRequestDTO.setKeyword("테스트"); // 검색 키워드 설정

        PageResultDTO<QnaBoardDTO, Object[]> result = qnaBoardService.getList(pageRequestDTO); // QnaBoardDTO 목록을 가져온다.-

        List<QnaBoardDTO> dtoList = result.getDtoList();

        System.out.println("게시글 리스트");
        // Loop through the DTO list
        for (QnaBoardDTO dto : dtoList) {
            Object[] objects = new String[]{dto.getContent()};
            System.out.println("게시글 번호 : " + dto.getBno());
            System.out.println("게시글 제목 : " + dto.getTitle());
            System.out.println("게시글 내용 : " + dto.getContent());
            System.out.println("=====");
        }
    }
}










