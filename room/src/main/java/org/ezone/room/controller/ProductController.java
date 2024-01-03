//package org.ezone.room.controller;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.ezone.room.dto.AccommodationDTO;
//import org.ezone.room.dto.ImgDTO;
//import org.ezone.room.dto.PageRequestDTO;
//import org.ezone.room.dto.PageResultDTO;
//import org.ezone.room.dto.ReservationDTO;
//import org.ezone.room.dto.ReviewBoardDTO;
//import org.ezone.room.entity.ReviewBoard;
//import org.ezone.room.manager.DateManager;
//import org.ezone.room.service.AccommodationService;
//import org.ezone.room.service.ReviewBoardService;
//import org.ezone.room.service.RoomService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//@RequestMapping("product")
//@Log4j2
//@RequiredArgsConstructor
//public class ProductController {
//
//    private final ReviewBoardService reviewBoardService;
////    private final AccommodationService accommodationService;
////    private final RoomService roomService;
//    @Autowired
//    DateManager dateManager;
//
//
//    @GetMapping("{ano}")
//    public String Accommodation(@PathVariable("ano") Long ano,
//                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
//                                Model model, ReservationDTO datedto) {
//        int pageSize = 5;
//
//        // 숙소 data 추출
//        AccommodationDTO accommodation = accommodationService.findByAno(ano);
//        double grade = Math.round(accommodation.getGrade() * 100) / 100.0;
//        accommodation.setGrade(grade);
//        List<ImgDTO> accommodationImgDTOS = accommodationService.getImgList(ano);
//
//        // ReviewBoard data 추출
//        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, pageSize);
//        PageResultDTO<ReviewBoardDTO, ReviewBoard> pageResultDTO = reviewBoardService.getReviewBoardsAndPageInfoByAccommodationId(ano, pageRequestDTO);
//        if(pageResultDTO.getTotalPage()==0){ pageResultDTO.setTotalPage(1);} // 글이 하나도 없을 땐 0으로 인식하므로
//
//        Map<Long, List<ImgDTO>> reviewBoardImgMap = new HashMap<>();
//        for (ReviewBoardDTO reviewBoardDTO : pageResultDTO.getDtoList()) {
//            List<ImgDTO> reviewBoardImgDTOS = reviewBoardService.getImgList(reviewBoardDTO.getRbno());
//            reviewBoardImgMap.put(reviewBoardDTO.getRbno(), reviewBoardImgDTOS);
//        }
//
//        //여기부터는 방 정보임
//        dateManager.CheckDate(datedto.getStartDate(),datedto.getEndDate()); //CheckDate
//        datedto = ReservationDTO.builder().StartDate(dateManager.getStartDate()).EndDate(dateManager.getEndDate()).build();
//        List<Object[]> list = roomService.getRvList(ano,datedto); //ano/date에 해당하는 list불러오기
//        //인덱스 0번 방정보 1번 이미지정보 2번 예약정보
//
//        model.addAttribute("R_list",list);
//        model.addAttribute("date",datedto);
//        model.addAttribute("accommodation", accommodation);
//        model.addAttribute("pageResultDTO", pageResultDTO);
//        model.addAttribute("reviewBoardImgMap", reviewBoardImgMap);
//        model.addAttribute("accommodationImgDTOS", accommodationImgDTOS);
//
//        return "product/accommodation";
//    }
//
//    @GetMapping("list")
//    public void list(@RequestParam(value = "category", required = false) String category,
//                     @RequestParam(value = "region", required = false) String region,
//                     @RequestParam(value = "page", required = false, defaultValue = "1") int page,
//                     @RequestParam(value = "inputedMinprice", required = false) Integer inputedMinprice,
//                     @RequestParam(value = "inputedMaxprice", required = false) Integer inputedMaxprice,
//                     PageRequestDTO pageRequestDTO, ReservationDTO datedto, Model model) {
//
//        int pageSize = 5;
//
//        dateManager.CheckDate(datedto.getStartDate(), datedto.getEndDate()); //데이트 매네져를 통한 간단한 체크방식
//        datedto = ReservationDTO.builder().StartDate(dateManager.getStartDate()).EndDate(dateManager.getEndDate()).build();
//
//        // 페이지 요청 객체 생성
//        pageRequestDTO = PageRequestDTO.builder()
//                .page(page)
//                .size(pageSize)
//                .build();
//
//        PageResultDTO<AccommodationDTO, Object[]> pageResult = accommodationService.searchAccommodationList
//                (pageRequestDTO, pageRequestDTO.getKeyword(), category, region,
//                        datedto.getStartDate(), datedto.getEndDate(), inputedMinprice, inputedMaxprice);
//        if(pageResult.getTotalPage()==0){ pageResult.setTotalPage(1);}
//
//        List<AccommodationDTO> updatedList = new ArrayList<>();
//        for (AccommodationDTO dto : pageResult.getDtoList()) {
//            double grade = Math.round(dto.getGrade() * 100) / 100.0;
//            dto.setGrade(grade);
//            updatedList.add(dto);
//        }
//        pageResult.setDtoList(updatedList);
//
//        model.addAttribute("pageResult", pageResult); // 숙소와 가격 정보를 담은 PageResultDTO
//        model.addAttribute("date", datedto);
//    }
//}
