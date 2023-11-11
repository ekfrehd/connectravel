package com.connectravel.service;

import com.connectravel.dto.AdminBoardDTO;
import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;

import java.util.List;

public interface AdminBoardService {

    Long registerAdminBoard(AdminBoardDTO dto);

    AdminBoardDTO getAdminBoard(Long abno);

    void updateAdminBoard(AdminBoardDTO adminBoardDTO);

    void deleteAdminBoard(Long abno);

    PageResultDTO<AdminBoardDTO, Object[]> getPaginatedAdminBoardList(PageRequestDTO pageRequestDTO, String category); // 리스트 출력

    List<ImgDTO> getAdminBoardImgList(Long abno);


}
