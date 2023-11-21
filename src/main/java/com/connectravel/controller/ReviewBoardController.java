package com.connectravel.controller;

import com.connectravel.domain.dto.ReviewBoardDTO;
import com.connectravel.domain.entity.Accommodation;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.Reservation;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ReservationRepository;
import com.connectravel.repository.ReviewBoardRepository;
import com.connectravel.service.ImgService;
import com.connectravel.service.ReviewBoardService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewBoardController {
    // list 출력은 ProductController에 있음

    private final ReservationRepository reservationRepository;

    private final MemberRepository memberRepository;

    private final ReviewBoardService reviewBoardService;

    private final ReviewBoardRepository reviewBoardRepository;

    private final ImgService imgService;


    @GetMapping("register/{rvno}")
    public String register(@PathVariable("rvno") Long rvno, RedirectAttributes redirectAttributes,
                           ReviewBoardDTO reviewBoardDTO, Model model, @AuthenticationPrincipal Member member) {
        // @AuthenticationPrincipal에서 직접 Member 객체를 사용
        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        Optional<Reservation> reservation = Optional.ofNullable(reservationRepository.findByRvno(rvno));

        if (!reservation.get().getMember().getId().toString().equals(member.getId().toString())) {
            log.info("다른사람껀 일단 리다이렉트");
            return "redirect:/";
        }



        if (reservation.isPresent()) {
            Accommodation accommodation = reservation.get().getRoom().getAccommodation();

            reviewBoardDTO.setWriterEmail(member.getEmail());
            reviewBoardDTO.setAno(accommodation.getAno());
            reviewBoardDTO.setRno(reservation.get().getRoom().getRno());
            reviewBoardDTO.setRvno(reservation.get().getRvno());
            model.addAttribute("dto", reviewBoardDTO);
            model.addAttribute("accommodationName", accommodation.getAccommodationName());
            model.addAttribute("roomName", reservation.get().getRoom().getRoomName());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Reservation not found");
        }
        return "member/review";
    }


    @PostMapping("register")
    public String register(@RequestParam("images") List<MultipartFile> images,
                           RedirectAttributes redirectAttributes, ReviewBoardDTO reviewBoardDTO, @AuthenticationPrincipal Member member) throws NotFoundException {


        Optional<Reservation> reservation = reservationRepository.findById(reviewBoardDTO.getRvno());

        if (reservation.isPresent()) {
            Accommodation accommodation = reservation.get().getRoom().getAccommodation();;

            reviewBoardDTO.setWriterEmail(member.getEmail());
            reviewBoardDTO.setAno(accommodation.getAno());
            reviewBoardDTO.setRno(reservation.get().getRoom().getRno());
            reviewBoardDTO.setRvno(reservation.get().getRvno());
            Long rbno = reviewBoardService.createReview(reviewBoardDTO);
            images.forEach(i -> {
                imgService.addReviewBoardImg(i,rbno);
            });
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Reservation not found");
        }

        // 파일등록 여부에따른 포인트 반영
        if(images == null){
            member.setPoint(member.getPoint()+200);
        }else {
            member.setPoint(member.getPoint()+300);
        }

        memberRepository.save(member);

        return "redirect:/reservation/list";
    }



}
