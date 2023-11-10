package com.connectravel.service;

import com.connectravel.dto.AdminBoardDTO;
import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;

import java.util.List;

public interface AdminBoardService {

    Long register(AdminBoardDTO dto); //등록

    AdminBoardDTO get(Long abno); //조회

    void modify(AdminBoardDTO adminBoardDTO); //수정

    void remove(Long abno); //삭제

    PageResultDTO<AdminBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO, String category); // 리스트 출력

    List<ImgDTO> getImgList(Long abno);


}
