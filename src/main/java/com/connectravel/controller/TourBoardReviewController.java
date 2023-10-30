package com.connectravel.controller;

import com.connectravel.repository.MemberRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.connectravel.dto.TourBoardDTO;
import com.connectravel.dto.TourBoardReivewDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.TourBoard;
import com.connectravel.repository.TourBoardRepository;
//import com.connectravel.security.CustomUserDetails;
import com.connectravel.service.ImgService;
import com.connectravel.service.TourBoardReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("tourreview")
public class TourBoardReviewController {

    private final TourBoardReviewService tourBoardReviewService;
    private final TourBoardRepository tourBoardRepository;
    private final ImgService imgService;
    private final MemberRepository memberRepository;


    @PostMapping("register")
    public String register(@RequestParam("images") List<MultipartFile> images,
                           TourBoardDTO tourBoardDTO, TourBoardReivewDTO tourBoardReivewDTO) throws NotFoundException {
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();*/
        String email = "1111@naver.com";
        Member member = memberRepository.findByEmail(email);

        TourBoard tourBoard = tourBoardRepository.findById(tourBoardDTO.getTbno())
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
