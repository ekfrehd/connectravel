package com.connectravel.service;

import com.connectravel.dto.AdminBoardDTO;
import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.entity.AdminBoard;
import com.connectravel.entity.Member;
import com.connectravel.repository.MemberRepository;

import java.util.List;

public interface AdminBoardService {

    Long register(AdminBoardDTO dto); //등록

    AdminBoardDTO get(Long bno); //조회

    void modify(AdminBoardDTO adminBoardDTO); //수정

    PageResultDTO<AdminBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO, String category); // 리스트 출력

    public List<ImgDTO> getImgList(Long bno);

    default AdminBoard dtoToEntity(AdminBoardDTO dto, MemberRepository memberRepository) {

        Member member = memberRepository.findByEmail(dto.getWriterEmail());
        // Member객체 생성 - 리포지토리 실행결과를 담는다.
        // Member member = new Member(); X
        // 새 객체 생성은 당연히 새로운 행을 만든다는 소리니까! 참조가 안됨!

        AdminBoard adminBoard = AdminBoard.builder().bno(dto.getBno()).title(dto.getTitle()).content(dto.getContent()).category(dto.getCategory()).member(member).build();
        //상단에서 생성한 Member객체 활용 Board객체 생성

        return adminBoard;
    }

    default AdminBoardDTO entityToDTO(AdminBoard adminBoard, Member member, Long adminReplyCount) {

        AdminBoardDTO adminBoardDTO = AdminBoardDTO.builder().bno(adminBoard.getBno()).title(adminBoard.getTitle()).content(adminBoard.getContent()).regDate(adminBoard.getRegTime()).modDate(adminBoard.getModTime()).category(adminBoard.getCategory()).writerEmail(member.getEmail()).writerName(member.getNickName()).replyCount(adminReplyCount.intValue()).build();

        return adminBoardDTO;
    }
}
