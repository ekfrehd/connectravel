package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.TourBoardDTO;

import java.util.List;

public interface TourBoardService {
    Long createTourBoard(TourBoardDTO dto);

    TourBoardDTO getTourBoard(Long tbno);

    void deleteTourBoard(Long tbno);

    void updateTourBoard(TourBoardDTO dto);

    PageResultDTO<TourBoardDTO, Object[]> getPaginatedTourBoardList(PageRequestDTO pageRequestDTO, String[] type, String keyword, String category, String region);

    List<ImgDTO> listTourBoardImages(Long tbno);

}
