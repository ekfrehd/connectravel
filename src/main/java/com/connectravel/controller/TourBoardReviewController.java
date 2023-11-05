package com.connectravel.controller;

import com.connectravel.domain.dto.MemberDTO;
import com.connectravel.domain.dto.TourBoardDTO;
import com.connectravel.domain.dto.TourBoardReviewDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.TourBoard;
import com.connectravel.repository.TourBoardRepository;
import com.connectravel.service.ImgService;
import com.connectravel.service.MemberService;
import com.connectravel.service.TourBoardReviewService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("tourreview")
public class TourBoardReviewController {

    private final TourBoardReviewService tourBoardReviewService;
    private final TourBoardRepository tourBoardRepository;
    private final ImgService ImgService;
    private final MemberService memberService;

    @PostMapping("register")
    public String register(@AuthenticationPrincipal Member member, Principal principal, @RequestParam("images") List<MultipartFile> images, TourBoardDTO tourBoardDTO, TourBoardReviewDTO tourBoardReivewDTO) throws NotFoundException {

        String username = principal.getName();
        MemberDTO memberDTO = memberService.getMember(member.getId());

        TourBoard tourBoard = tourBoardRepository.findById(tourBoardDTO.getTbno()).orElseThrow(() -> new NotFoundException("TourBoard not found"));

        /*tourBoardReivewDTO.setWriterEmail(member.getEmail());*/
        tourBoardReivewDTO.setTbno(tourBoard.getTbno());
        Long trbno = tourBoardReviewService.createTourBoardReview(tourBoardReivewDTO, member.getEmail());
        images.forEach(i -> {
            ImgService.addTourBoardReviewImg(i, trbno);
        });

        return "redirect:/tour/read?tbno=" + tourBoardDTO.getTbno();
    }

    @PostMapping("remove")
    public String remove(Long tbrno, Long tbno, TourBoardDTO tourBoardDTO, RedirectAttributes redirectAttributes) throws NotFoundException {

        tourBoardReviewService.deleteTourBoardReviewWithReplies(tbrno);

        redirectAttributes.addFlashAttribute("msg", tbrno);

        return "redirect:/tour/read?tbno=" + tbno;
    }
}
