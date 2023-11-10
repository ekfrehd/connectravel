package com.connectravel.repository;

import com.connectravel.constant.Role;
import com.connectravel.entity.AdminBoard;
import com.connectravel.entity.AdminReply;
import com.connectravel.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class AdminBoardRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(AdminBoardRepositoryTest.class);

    @Autowired
    private AdminBoardRepository adminBoardRepository;
    @Autowired
    private MemberRepository memberRepository; // Member 엔티티를 위한 레포지토리
    @Autowired
    private AdminReplyRepository adminReplyRepository; // AdminReply 엔티티를 위한 레포지토리

    // 테스트에 필요한 데이터 설정
    @BeforeEach
    public void setUp() {
        // 회원(Member) 인스턴스 생성
        Member member = Member.builder()
                .email("test@example.com")
                .password("password")
                .name("홍길동")
                .nickName("gildong")
                .tel("010-1234-5678")
                .point(1000)
                .role(Role.ADMIN)
                .build();
        member = memberRepository.save(member); // 저장

        // 관리자 게시판(AdminBoard) 인스턴스 생성 및 저장
        AdminBoard adminBoard = AdminBoard.builder()
                .title("테스트 게시글")
                .content("테스트 내용입니다.")
                .category("공지사항")
                .member(member)
                .build();
        adminBoard = adminBoardRepository.save(adminBoard); // 저장

        // 관리자 댓글(AdminReply) 인스턴스 생성 및 저장
        AdminReply adminReply = AdminReply.builder()
                .content("테스트 댓글입니다.")
                .member(member)
                .adminBoard(adminBoard)
                .build();
        adminReplyRepository.save(adminReply); // 저장
    }

    @Test
    public void testSearchPageAdminBoard() {
        // 검색 매개변수 정의
        String[] type = {"t"}; // 예시: 제목으로 검색
        String category = "category1"; // 예시 카테고리
        String keyword = "keyword"; // 예시 키워드
        Pageable pageable = PageRequest.of(0, 10); // 예시 페이지네이션

        // 테스트 대상 메서드 호출
        Page<Object[]> page = adminBoardRepository.searchPageAdminBoard(type, category, keyword, pageable);

        // 검증 수행
        assertNotNull(page, "결과 페이지는 null이 아니어야 합니다");
        assertEquals(10, page.getSize(), "페이지 크기는 10이어야 합니다");

        // 로그로 결과 확인
        if (log.isDebugEnabled()) {
            log.debug("검색된 페이지 정보: 페이지 번호 = {}, 페이지 크기 = {}", page.getNumber(), page.getSize());
            page.getContent().forEach(objects -> log.debug("게시글: {}, 작성자: {}, 댓글 수: {}", objects));
        }
    }
}
