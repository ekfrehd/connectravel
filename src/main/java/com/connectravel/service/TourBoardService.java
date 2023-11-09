package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.TourBoardDTO;
import com.connectravel.entity.TourBoard;

import java.util.List;

public interface TourBoardService {
    Long register(TourBoardDTO dto);

    TourBoardDTO read(Long tbno);

    void remove(Long tbno);

    void modify(TourBoardDTO dto);

    PageResultDTO<TourBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO, String[] type, String keyword, String category, String region, String address);

    List<ImgDTO> getImgList(Long tbno);

    default TourBoard dtoToEntity(TourBoardDTO dto) {
        TourBoard entity = TourBoard.builder().tbno(dto.getTbno()).postal(dto.getPostal()).address(dto.getAddress()).title(dto.getTitle()).content(dto.getContent()).category(dto.getCategory()).region(dto.getRegion()).address(dto.getAddress()).build();
        return entity;
    }

    default TourBoardDTO entityToDto(TourBoard entity) {
        TourBoardDTO dto = TourBoardDTO.builder().tbno(entity.getTbno()).content(entity.getContent()).title(entity.getTitle()).postal(entity.getPostal()).region(entity.getRegion()).address(entity.getAddress()).category(entity.getCategory()).grade(entity.getGrade()).reviewCount(entity.getReviewCount()).regDate(entity.getRegTime()).modDate(entity.getModTime()).address(entity.getAddress()).build();

        return dto;
    }
}
