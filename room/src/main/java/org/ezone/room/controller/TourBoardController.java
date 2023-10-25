
package org.ezone.room.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.dto.*;
import org.ezone.room.entity.Member;
import org.ezone.room.entity.ReviewBoard;
import org.ezone.room.entity.TourBoardReview;
import org.ezone.room.repository.TourBoardReviewRepository;
import org.ezone.room.security.CustomUserDetails;
import org.ezone.room.service.ImgService;
import org.ezone.room.service.TourBoardReviewService;
import org.ezone.room.service.TourBoardService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("tour")
@Log4j2
@RequiredArgsConstructor
public class TourBoardController {

    private final TourBoardService service;

    private final TourBoardReviewService tourBoardReviewService;

    private final ImgService imgService;

    @GetMapping("list")
    public String list(PageRequestDTO pageRequestDTO, Model model){

        pageRequestDTO.setSize(9);
        PageResultDTO<TourBoardDTO, Object[]> tourBoard = service.getList(pageRequestDTO, pageRequestDTO.getType(),
                pageRequestDTO.getCategory(), pageRequestDTO.getKeyword(), pageRequestDTO.getRegion());
        if(tourBoard.getTotalPage()==0){ tourBoard.setTotalPage(1);} // 글이 하나도 없을 땐 0으로 인식하므로

        model.addAttribute("result", tourBoard);
        return "tour/list";
    }

    @GetMapping("register")
    public void register(){
        log.info("register get...");
    }

    @PostMapping("register")
    public String registerPost(@RequestParam("images") List<MultipartFile> images,
                               TourBoardDTO dto, RedirectAttributes redirectAttributes){

        int firstSpaceIndex = dto.getAddress().indexOf(" ");
        int secondSpaceIndex = dto.getAddress().indexOf(" ", firstSpaceIndex + 1);
        dto.setRegion(dto.getAddress().substring(0, secondSpaceIndex));

        // 새로 추가된 엔티티의 번호
        Long tbno = service.register(dto);

        redirectAttributes.addFlashAttribute("msg", tbno);

        images.forEach(i -> {
            imgService.TourBoardRegister(i,tbno);
        });

        return "redirect:/tour/list";

    }

    @GetMapping({"read", "modify"})
    public void read(@RequestParam(value = "review", required = false, defaultValue = "1") int page,
                      long tbno, @ModelAttribute("requestDTO")PageRequestDTO requestDTO, Model model){

        int pageSize = 5;

        // 게시물 get
        TourBoardDTO dto = service.read(tbno);
        log.info(dto);
        double grade = Math.round(dto.getGrade() * 100) / 100.0;
        dto.setGrade(grade);
        List<ImgDTO> tourBoardImgDTOS = service.getImgList(tbno);

        // ReviewBoard data 추출
        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, pageSize);
        PageResultDTO<TourBoardReivewDTO, TourBoardReview> pageResultDTO = tourBoardReviewService.getTourReviewBoardsAndPageInfoByTourBoardId(tbno, pageRequestDTO);
        if(pageResultDTO.getTotalPage()==0){ pageResultDTO.setTotalPage(1);} // 글이 하나도 없을 땐 0으로 인식하므로

        // PaeeRueslt를 통한 trbno추출 -> 이미지 리스트화
        Map<Long, List<ImgDTO>> tourBoardReviewImgDTOsImgMap = new HashMap<>();
        for (TourBoardReivewDTO tourBoardReivewDTO : pageResultDTO.getDtoList()) {
            log.info(tourBoardReivewDTO.getTbrno());
            List<ImgDTO> tourBoardReviewImgDTOS = tourBoardReviewService.getImgList(tourBoardReivewDTO.getTbrno());
            tourBoardReviewImgDTOsImgMap.put(tourBoardReivewDTO.getTbrno(), tourBoardReviewImgDTOS);
            log.info(tourBoardReviewImgDTOsImgMap.get(tourBoardReivewDTO.getTbrno()));
        }

        model.addAttribute("tourBoardImgDTOS", tourBoardImgDTOS);
        model.addAttribute("tourboard", dto);
        model.addAttribute("pageResultDTO", pageResultDTO);
        model.addAttribute("grade", grade);
        model.addAttribute("tourBoardReviewImgDTOsImgMap", tourBoardReviewImgDTOsImgMap);
    }

    @PostMapping("remove")
    public String remove(long tbno, RedirectAttributes redirectAttributes){

        service.remove(tbno);

        redirectAttributes.addFlashAttribute("msg", tbno);

        return "redirect:/tour/list";
    }

    @PostMapping("modify")
    public String modify(TourBoardDTO dto, @ModelAttribute("requesetDTO")PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes){

        int firstSpaceIndex = dto.getAddress().indexOf(" ");
        int secondSpaceIndex = dto.getAddress().indexOf(" ", firstSpaceIndex + 1);
        dto.setRegion(dto.getAddress().substring(0, secondSpaceIndex));

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("tbno", dto.getTbno());
        redirectAttributes.addAttribute("category", dto.getCategory());

        return "redirect:/tour/read";
    }
}
