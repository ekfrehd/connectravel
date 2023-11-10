package com.connectravel.service;

import com.connectravel.constant.Role;
import com.connectravel.dto.AdminBoardDTO;
import com.connectravel.entity.AdminBoard;
import com.connectravel.entity.Member;
import com.connectravel.repository.AdminBoardRepository;
import com.connectravel.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AdminBoardServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AdminBoardServiceTest.class);

    @Autowired
    private AdminBoardService adminBoardService;

    @Autowired
    private AdminBoardRepository adminBoardRepository;

    @Autowired
    private MemberRepository memberRepository;

    private AdminBoardDTO adminBoardDTO;

    // 테스트에 필요한 기본 데이터 설정
    @BeforeEach
    public void setUp() {
        Member member = memberRepository.save(Member.builder()
                .email("adminTest@example.com")
                .password("password")
                .name("홍길동")
                .nickName("gildong")
                .tel("010-1234-5678")
                .point(1000)
                .role(Role.ADMIN)
                .build());

        AdminBoard adminBoard = adminBoardRepository.save(AdminBoard.builder()
                .title("테스트 게시글")
                .content("테스트 내용입니다.")
                .category("공지사항")
                .member(member)
                .build());

        adminBoardDTO = new AdminBoardDTO();
        adminBoardDTO.setAbno(adminBoard.getAbno());
        adminBoardDTO.setTitle(adminBoard.getTitle());
        adminBoardDTO.setContent(adminBoard.getContent());
        adminBoardDTO.setWriterEmail(member.getEmail());
    }

    @Test
    @Transactional
    public void testRegisterAdminBoard() {
        // 관리자 게시글 등록 테스트
        Long abno = adminBoardService.registerAdminBoard(adminBoardDTO);
        assertNotNull(abno, "게시글 등록 후 ID를 반환해야 합니다.");

        AdminBoard adminBoard = adminBoardRepository.findById(abno).orElse(null);
        assertNotNull(adminBoard, "저장된 게시글을 찾을 수 있어야 합니다.");
        assertEquals(adminBoardDTO.getTitle(), adminBoard.getTitle(), "저장된 게시글의 제목이 일치해야 합니다.");

        log.debug("등록된 게시글 ID: {}", abno);
        log.debug("등록된 게시글 제목: {}", adminBoard.getTitle());

    }
/*
    @Test
    public void testGetAdminBoard() {
        // 관리자 게시글 조회 테스트
        AdminBoardDTO foundAdminBoardDTO = adminBoardService.getAdminBoard(adminBoardDTO.getAbno());
        assertNotNull(foundAdminBoardDTO, "게시글 조회 결과는 null이 아니어야 합니다.");
        assertEquals(adminBoardDTO.getTitle(), foundAdminBoardDTO.getTitle(), "조회된 게시글의 제목이 일치해야 합니다.");

        log.debug("조회된 게시글 ID: {}", foundAdminBoardDTO.getAbno());
        log.debug("조회된 게시글 제목: {}", foundAdminBoardDTO.getTitle());

    }

    @Test
    @Transactional
    public void testUpdateAdminBoard() {
        // 관리자 게시글 수정 테스트
        adminBoardDTO.setTitle("Updated Title");
        adminBoardService.updateAdminBoard(adminBoardDTO);

        AdminBoard updatedAdminBoard = adminBoardRepository.findById(adminBoardDTO.getAbno()).orElse(null);
        assertNotNull(updatedAdminBoard, "수정된 게시글을 찾을 수 있어야 합니다.");
        assertEquals("Updated Title", updatedAdminBoard.getTitle(), "수정된 게시글의 제목이 일치해야 합니다.");

        log.debug("수정된 게시글 ID: {}", updatedAdminBoard.getAbno());
        log.debug("수정된 게시글 제목: {}", updatedAdminBoard.getTitle());

    }

    // ...

    @Test
    @Transactional
    public void testDeleteAdminBoard() {
        // 관리자 게시글 삭제 테스트
        adminBoardService.deleteAdminBoard(adminBoardDTO.getAbno());
        boolean exists = adminBoardRepository.existsById(adminBoardDTO.getAbno());
        assertFalse(exists, "게시글이 삭제된 후에는 더 이상 존재하지 않아야 합니다.");

        log.debug("삭제된 게시글 ID: {}", adminBoardDTO.getAbno());
        log.debug("게시글 존재 여부: {}", exists ? "예" : "아니오");
    }

    @Test
    public void testGetPaginatedAdminBoardList() {
        // 관리자 게시글 페이지네이션 목록 조회 테스트
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(1);
        pageRequestDTO.setSize(10);

        PageResultDTO<AdminBoardDTO, Object[]> pageResultDTO = adminBoardService.getPaginatedAdminBoardList(pageRequestDTO, "category1");

        assertNotNull(pageResultDTO, "페이지 결과는 null이 아니어야 합니다.");
        assertEquals(1, pageResultDTO.getDtoList().size(), "조회된 페이지의 게시글 수는 요청한 페이지 크기와 일치해야 합니다.");

        // 로그를 통한 결과 확인
        log.debug("총 페이지 수: {}", pageResultDTO.getTotalPage());
        log.debug("전체 게시글 수: {}", pageResultDTO.getTotalSize());
        pageResultDTO.getDtoList().forEach(dto -> log.debug("게시글 제목: {}", dto.getTitle()));
    }*/

}
