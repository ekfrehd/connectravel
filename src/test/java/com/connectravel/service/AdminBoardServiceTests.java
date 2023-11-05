package com.connectravel.service;

import com.connectravel.dto.AdminBoardDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.repository.AdminBoardRepository;
import com.connectravel.repository.MemberRepository;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class AdminBoardServiceTests {

    @Autowired
    private AdminBoardRepository adminBoardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AdminBoardService adminBoardService;

    @Test // 게시글 추가 테스트
    public void testRegisterAdminBoard() {

        String[] sampleText = { // 질문을 배열로 저장
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

        IntStream.rangeClosed(1, 10).forEach(i -> { // 10번 반복
            AdminBoardDTO adminBoardDTO = new AdminBoardDTO(); // 테스트 QnaBoardDTO 생성
            adminBoardDTO.setTitle(sampleText[i - 1]); // 게시글 제목 입력
            adminBoardDTO.setCategory("faq");
            adminBoardDTO.setContent(sampleText[i - 1]); // 게시글 내용 입력
            adminBoardDTO.setWriterEmail("sample@sample.com"); // 작성자 이메일 입력

            Long bno = adminBoardService.register(adminBoardDTO); // 게시글 등록 register 메서드 호출
            System.out.println(bno + "번 게시물 등록");
        });
    }

    @Test // 게시글 조회 테스트
    public void testGetQnaBoard() {
        Long bno = 1L; // 테스트 게시글 번호

        AdminBoardDTO result = adminBoardService.get(bno); // 게시글 조회 get 메서드 호출

        System.out.println(bno + "번 게시글");
        System.out.println("QnaBoard 정보: " + result.toString());
        System.out.println("Member 정보: " + result.getWriterEmail() + ", " + result.getWriterName());
    }

    @Test // 게시글 수정 테스트
    public void testModifyQnaBoard() {
        Long bno = 1L; // 수정할 게시글 번호

        AdminBoardDTO adminBoardDTO = new AdminBoardDTO(); // 테스트 QnaBoardDTO 생성 (수정할 내용 포함)
        adminBoardDTO.setBno(bno); // 게시글 번호
        adminBoardDTO.setTitle("수정된 제목"); // 수정할 번호
        adminBoardDTO.setContent("수정된 내용"); // 수정할 내용
        adminBoardDTO.setCategory("이벤트"); // 수정할 내용

        adminBoardService.modify(adminBoardDTO); // 게시글 수정

        AdminBoardDTO result = adminBoardService.get(bno); // 수정한 게시글 가져오기
        System.out.println("AdminBoardDTO 정보: " + result.toString());
    }

    @Test // 게시글 삭제 테스트
    public void testRemoveAdminBoard() {
        Long bno = 12L; // 삭제할 게시글 번호

        try {
            adminBoardService.remove(bno);// 게시글 삭제 메서드 호출
        } catch (Exception e) {
            // 삭제할 게시글이 존재하지 않을 경우에 대한 예외 처리
            // 예외가 발생해도 테스트가 실패하지 않도록 처리
            System.out.println("삭제할 게시글이 존재하지 않습니다.");
        }
    }

    @Test // 게시글 리스트 조회
    @Transactional
    public void testGetList() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(1); // 1 페이지
        pageRequestDTO.setSize(10); // 10개 출력
        pageRequestDTO.setType("t"); // 검색 타입 설정, 제목
        pageRequestDTO.setKeyword("?"); // 검색 키워드 설정

        PageResultDTO<AdminBoardDTO, Object[]> result = adminBoardService.getList(pageRequestDTO, "faq"); // QnaBoardDTO 목록을 가져온다.

        List<AdminBoardDTO> dtoList = result.getDtoList(); // QnaBoardDTO 리스트를 dtoList 변수에 할당

        System.out.println("게시글 리스트");
        for (AdminBoardDTO dto : dtoList) {
            Object[] objects = new String[]{dto.getContent()};
            System.out.println("게시글 번호 : " + dto.getBno());
            System.out.println("게시글 제목 : " + dto.getTitle());
            System.out.println("게시글 카테고리 : " + dto.getCategory());
            System.out.println("게시글 내용 : " + dto.getContent());
            System.out.println("=====");
        }
    }
}









