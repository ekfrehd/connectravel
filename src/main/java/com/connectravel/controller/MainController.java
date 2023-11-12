package com.connectravel.controller;

import com.connectravel.dto.AccommodationDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.TourBoardDTO;
import com.connectravel.service.AccommodationService;
import com.connectravel.service.TourBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {

    private final TourBoardService tourBoardService;
    private final AccommodationService accommodationService;


    @GetMapping(value = "/")
    public String main(Model model){
        int page = 1;
        int pageSize=10;
        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, pageSize);
        PageResultDTO<TourBoardDTO, Object[]> tourBoard = tourBoardService.getPaginatedTourBoardList(pageRequestDTO,
                pageRequestDTO.getType(), pageRequestDTO.getKeyword(), pageRequestDTO.getCategory(), pageRequestDTO.getRegion());

        model.addAttribute("tourBoard", tourBoard);

        return "main";
    }

    @GetMapping(value = "search")
    public String search(@RequestParam("keyword") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model){

        int pageSize = 10;
        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, pageSize);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);

        Integer min = null, max = null;

        PageResultDTO<TourBoardDTO, Object[]> tourBoard = tourBoardService.getPaginatedTourBoardList(pageRequestDTO,
                pageRequestDTO.getType(), keyword, pageRequestDTO.getCategory(), pageRequestDTO.getRegion());
        PageResultDTO<AccommodationDTO, Object[]> accommodation = accommodationService.searchAccommodationList(pageRequestDTO,
                keyword, pageRequestDTO.getCategory(), pageRequestDTO.getRegion(),  startDate, endDate, min, max);

        model.addAttribute("tourBoard", tourBoard);
        model.addAttribute("accommodation", accommodation);

        return "search";
    }
}
