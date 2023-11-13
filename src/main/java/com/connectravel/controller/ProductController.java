package com.connectravel.controller;

import com.connectravel.domain.dto.*;
import com.connectravel.domain.entity.ReviewBoard;
import com.connectravel.manager.DateManager;
import com.connectravel.service.AccommodationService;
import com.connectravel.service.ReviewBoardService;
import com.connectravel.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("product")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final AccommodationService accommodationService;
    private final ReviewBoardService reviewBoardService;
    private final RoomService roomService;

    @Autowired
    private DateManager dateManager;

    @GetMapping("list")
    public void list(@RequestParam(value = "category", required = false) String category,
                     @RequestParam(value = "region", required = false) String region,
                     @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                     @RequestParam(value = "inputedMinprice", required = false) Integer inputedMinprice,
                     @RequestParam(value = "inputedMaxprice", required = false) Integer inputedMaxprice,
                     PageRequestDTO pageRequestDTO, ReservationDTO datedto, Model model) {

        int pageSize = 5;

        // DateManager를 통한 날짜 체크
        dateManager.checkDate(datedto.getStartDate(), datedto.getEndDate());
        datedto = ReservationDTO.builder().startDate(dateManager.getStartDate()).endDate(dateManager.getEndDate()).build();

        // 페이지 요청 객체 생성
        pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(pageSize)
                .build();

        PageResultDTO<AccommodationDTO, Object[]> pageResult = accommodationService.searchAccommodationList
                (pageRequestDTO, pageRequestDTO.getKeyword(), category, region,
                        datedto.getStartDate(), datedto.getEndDate(), inputedMinprice, inputedMaxprice);
        if(pageResult.getTotalPage()==0){ pageResult.setTotalPage(1);}

        List<AccommodationDTO> updatedList = new ArrayList<>();
        for (AccommodationDTO dto : pageResult.getDtoList()) {
            double grade = Math.round(dto.getGrade() * 100) / 100.0;
            dto.setGrade(grade);
            updatedList.add(dto);
        }
        pageResult.setDtoList(updatedList);

        model.addAttribute("pageResult", pageResult); // 숙소와 가격 정보를 담은 PageResultDTO
        model.addAttribute("date", datedto);
    }

    @GetMapping("{ano}")
    public String accommodationDetails(@PathVariable("ano") Long ano,
                                       @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                       Model model, ReservationDTO datedto) {
        int pageSize = 5;

        // 숙소 데이터 추출
        AccommodationDTO accommodation = accommodationService.findByAno(ano);
        double grade = Math.round(accommodation.getGrade() * 100) / 100.0;
        accommodation.setGrade(grade);
        List<ImgDTO> accommodationImgDTOS = accommodationService.getImgList(ano);

        // 리뷰 데이터 추출
        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, pageSize);
        PageResultDTO<ReviewBoardDTO, ReviewBoard> pageResultDTO = reviewBoardService.getPaginatedReviewsByAccommodation(ano, pageRequestDTO);
        if(pageResultDTO.getTotalPage() == 0) { pageResultDTO.setTotalPage(1); } // 글이 하나도 없을 때 0으로 인식하므로

        Map<Long, List<ImgDTO>> reviewBoardImgMap = new HashMap<>();
        for (ReviewBoardDTO reviewBoardDTO : pageResultDTO.getDtoList()) {
            List<ImgDTO> reviewBoardImgDTOS = reviewBoardService.getImgList(reviewBoardDTO.getRbno());
            reviewBoardImgMap.put(reviewBoardDTO.getRbno(), reviewBoardImgDTOS);
        }

        dateManager.checkDate(datedto.getStartDate(), datedto.getEndDate());
        datedto = ReservationDTO.builder()
                .startDate(dateManager.getStartDate())
                .endDate(dateManager.getEndDate())
                .build();

        List<Object[]> list = roomService.findRoomsAndReservationsByAccommodationAndDate(
                ano,
                datedto.getStartDate(),
                datedto.getEndDate()
        );

        // 모델에 데이터 추가
        model.addAttribute("R_list", list);
        model.addAttribute("date", datedto);
        model.addAttribute("accommodation", accommodation);
        model.addAttribute("pageResultDTO", pageResultDTO);
        model.addAttribute("reviewBoardImgMap", reviewBoardImgMap);
        model.addAttribute("accommodationImgDTOS", accommodationImgDTOS);

        return "product/accommodation";
    }

}
