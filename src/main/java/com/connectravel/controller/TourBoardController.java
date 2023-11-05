package com.connectravel.controller;

import com.connectravel.domain.dto.*;
import com.connectravel.domain.entity.TourBoardReview;
import com.connectravel.service.TourBoardReviewService;
import com.connectravel.service.TourBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("tour")
@Log4j2
@RequiredArgsConstructor
public class TourBoardController {

    private final TourBoardService tourBoardService;
    private final TourBoardReviewService tourBoardReviewService;
    private final com.connectravel.service.ImgService ImgService;

    @GetMapping("list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {

        pageRequestDTO.setSize(9); // 한 페이지 9개 리뷰 출력
        PageResultDTO<TourBoardDTO, Object[]> tourBoard = tourBoardService.getPaginatedTourBoardList(pageRequestDTO, pageRequestDTO.getType(), pageRequestDTO.getCategory(), pageRequestDTO.getKeyword(), pageRequestDTO.getRegion(), pageRequestDTO.getAddress());
        if (tourBoard.getTotalPage() == 0) {
            tourBoard.setTotalPage(1);
        } // 글이 하나도 없을 땐 0으로 인식하므로

        model.addAttribute("result", tourBoard);
        tourBoard.getDtoList().forEach(obj -> log.info(obj));
        return "tour/list";
    }

    @GetMapping("register")
    public void register() {
        log.info("register get...");
    }

    @PostMapping("register")
    public String registerPost(@RequestParam("images") List<MultipartFile> images, TourBoardDTO dto, RedirectAttributes redirectAttributes) {

        int firstSpaceIndex = dto.getAddress().indexOf(" ");
        int secondSpaceIndex = dto.getAddress().indexOf(" ", firstSpaceIndex + 1);
        dto.setRegion(dto.getAddress().substring(0, secondSpaceIndex));

        // 새로 추가된 엔티티의 번호
        Long tbno = tourBoardService.createTourBoard(dto);

        redirectAttributes.addFlashAttribute("msg", tbno);

        images.forEach(i -> {
            ImgService.addTourBoardImg(i, tbno);
        });

        return "redirect:/tour/list";

    }

    @GetMapping({"read", "modify"})
    public void read(@RequestParam(value = "review", required = false, defaultValue = "1") int page, long tbno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {

        int pageSize = 5;

        // 게시물 get
        TourBoardDTO dto = tourBoardService.getTourBoard(tbno);
        log.info(dto);
        double grade = Math.round(dto.getGrade() * 100) / 100.0;
        dto.setGrade(grade);
        List<ImgDTO> tourBoardImgDTOS = tourBoardService.listTourBoardImages(tbno);

        // ReviewBoard data 추출
        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, pageSize);
        PageResultDTO<TourBoardReviewDTO, TourBoardReview> pageResultDTO = tourBoardReviewService.getPaginatedTourBoardReviews(tbno, pageRequestDTO);
        if (pageResultDTO.getTotalPage() == 0) {
            pageResultDTO.setTotalPage(1);
        } // 글이 하나도 없을 땐 0으로 인식하므로

        // PaeeRueslt를 통한 trbno추출 -> 이미지 리스트화
        Map<Long, List<ImgDTO>> tourBoardReviewImgDTOsImgMap = new HashMap<>();
        for (TourBoardReviewDTO tourBoardReivewDTO : pageResultDTO.getDtoList()) {
            log.info(tourBoardReivewDTO.getTbrno());
            List<ImgDTO> tourBoardReviewImgDTOS = tourBoardReviewService.listTourBoardReviewImages(tourBoardReivewDTO.getTbrno());
            tourBoardReviewImgDTOsImgMap.put(tourBoardReivewDTO.getTbrno(), tourBoardReviewImgDTOS);
            log.info(tourBoardReviewImgDTOsImgMap.get(tourBoardReivewDTO.getTbrno()));
        }

        model.addAttribute("tourBoardImgDTOS", tourBoardImgDTOS);
        model.addAttribute("tourboard", dto);
        model.addAttribute("pageResultDTO", pageResultDTO);
        model.addAttribute("grade", grade);
        model.addAttribute("tourBoardReviewImgDTOsImgMap", tourBoardReviewImgDTOsImgMap);
    }

    @PostMapping("modify")
    public String modify(TourBoardDTO dto, @ModelAttribute("requesetDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {

        int firstSpaceIndex = dto.getAddress().indexOf(" ");
        int secondSpaceIndex = dto.getAddress().indexOf(" ", firstSpaceIndex + 1);
        dto.setRegion(dto.getAddress().substring(0, secondSpaceIndex));

        tourBoardService.updateTourBoard(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("tbno", dto.getTbno());
        redirectAttributes.addAttribute("category", dto.getCategory());

        return "redirect:/tour/read";
    }

    @PostMapping("remove")
    public String remove(long tbno, RedirectAttributes redirectAttributes) {

        tourBoardService.deleteTourBoard(tbno);

        redirectAttributes.addFlashAttribute("msg", tbno);

        return "redirect:/tour/list";
    }
}
