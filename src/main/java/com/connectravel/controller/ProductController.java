package com.connectravel.controller;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.dto.PageRequestDTO;
import com.connectravel.domain.dto.PageResultDTO;
import com.connectravel.domain.dto.ReservationDTO;
import com.connectravel.manager.DateManager;
import com.connectravel.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("product")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final AccommodationService accommodationService;

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
}
