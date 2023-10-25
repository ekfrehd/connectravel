package org.ezone.room.controller;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.dto.TourBoardDTO;
import org.ezone.room.dto.TourBoardReivewDTO;
import org.ezone.room.entity.Member;
import org.ezone.room.entity.TourBoard;
import org.ezone.room.repository.TourBoardReviewRepository;
import org.ezone.room.repository.TourRepository;
import org.ezone.room.security.CustomUserDetails;
import org.ezone.room.service.ImgService;
import org.ezone.room.service.TourBoardReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("tourreview")
public class TourBoardReviewController {

    private final TourBoardReviewService tourBoardReviewService;
    private final TourRepository tourRepository;
    private final ImgService imgService;


    @PostMapping("register")
    public String register(@RequestParam("images") List<MultipartFile> images,
                           TourBoardDTO tourBoardDTO, TourBoardReivewDTO tourBoardReivewDTO) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        TourBoard tourBoard = tourRepository.findById(tourBoardDTO.getTbno())
                .orElseThrow(() -> new NotFoundException("TourBoard not found"));

        tourBoardReivewDTO.setWriterEmail(member.getEmail());
        tourBoardReivewDTO.setTbno(tourBoard.getTbno());
        Long trbno = tourBoardReviewService.register(tourBoardReivewDTO);
        images.forEach(i -> {
            imgService.TourBoardReviewRegister(i, trbno);
        });

        return "redirect:/tour/read?tbno="+tourBoardDTO.getTbno();
    }

    @PostMapping("remove")
    public String remove(Long tbrno, Long tbno, TourBoardDTO tourBoardDTO, RedirectAttributes redirectAttributes) throws NotFoundException {

        tourBoardReviewService.removeWithReplies(tbrno, tbno);

        redirectAttributes.addFlashAttribute("msg", tbrno);

        return "redirect:/tour/read?tbno="+tbno;
    }
}
