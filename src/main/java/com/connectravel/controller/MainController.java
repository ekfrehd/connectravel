package com.connectravel.controller;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.dto.PageRequestDTO;
import com.connectravel.domain.dto.PageResultDTO;
import com.connectravel.domain.dto.TourBoardDTO;
import com.connectravel.service.SearchService;
import com.connectravel.service.TourBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {

    private final TourBoardService tourBoardService;

    private final SearchService searchService;

    @GetMapping(value = "/")
    public String main(Model model) {

        int page = 1;
        int pageSize=10;

        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, pageSize);
        PageResultDTO<TourBoardDTO, Object[]> tourBoard = tourBoardService.getPaginatedTourBoardList(pageRequestDTO,
                pageRequestDTO.getType(), pageRequestDTO.getKeyword(), pageRequestDTO.getCategory(), pageRequestDTO.getRegion(), pageRequestDTO.getAddress());

        model.addAttribute("tourBoard", tourBoard);

        return "main";
    }

    @GetMapping(value = "search")
    public String search(@RequestParam("keyword") String keyword,
                         @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {

        int pageSize = 10;
        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, pageSize);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);

        Integer min = null, max = null;

        // 옵션 ID 세트를 빈 Set으로 초기화
        Set<Long> optionIds = new HashSet<>();

        PageResultDTO<TourBoardDTO, Object[]> tourBoard = tourBoardService.getPaginatedTourBoardList(pageRequestDTO,
                pageRequestDTO.getType(), keyword, pageRequestDTO.getCategory(), pageRequestDTO.getRegion(), pageRequestDTO.getAddress());
        PageResultDTO<AccommodationDTO, Object[]> accommodation = searchService.searchAccommodationList(pageRequestDTO,
                keyword, pageRequestDTO.getAccommodationType(), pageRequestDTO.getRegion(), startDate, endDate, min, max, optionIds);

        model.addAttribute("tourBoard", tourBoard);
        model.addAttribute("accommodation", accommodation);

        return "search";
    }


}