package org.ezone.room.controller;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.repository.MemberRepository;
import org.ezone.room.repository.ReviewBoardRepository;
import org.ezone.room.security.CustomUserDetails;
import org.ezone.room.dto.ReviewBoardDTO;
import org.ezone.room.entity.AccommodationEntity;
import org.ezone.room.entity.Member;
import org.ezone.room.entity.ReservationEntity;
import org.ezone.room.repository.ReservationRepository;
import org.ezone.room.service.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String goRegister(@PathVariable("rvno") Long rvno, RedirectAttributes redirectAttributes,
                             ReviewBoardDTO reviewBoardDTO, Model model) throws NotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = null;
        if (principal instanceof CustomUserDetails) {
            member = ((CustomUserDetails) principal).getMember();
        }
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        Optional<ReservationEntity> reservation = Optional.ofNullable(reservationRepository.findByRvno(rvno));

        if (!reservation.get().getMember_id().getId().toString().equals(member.getId().toString())) {
            log.info("다른사람껀 일단 리다이렉트");
            return "redirect:/";
        }



        if (reservation.isPresent()) {
            AccommodationEntity accommodation = reviewBoardService.findAccommodationByRoomId(reservation.get().getRoom_id().getRno());

            reviewBoardDTO.setWriterEmail(member.getEmail());
            reviewBoardDTO.setAno(accommodation.getAno());
            reviewBoardDTO.setRno(reservation.get().getRoom_id().getRno());
            reviewBoardDTO.setRvno(reservation.get().getRvno());
            model.addAttribute("dto", reviewBoardDTO);
            model.addAttribute("accommodationName", accommodation.getName());
            model.addAttribute("roomName", reservation.get().getRoom_id().getRoom_name());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Reservation not found");
        }
        return "member/review";
    }

    @PostMapping("register")
    public String register(@RequestParam("images") List<MultipartFile> images,
                           RedirectAttributes redirectAttributes, ReviewBoardDTO reviewBoardDTO) throws NotFoundException {
        if (reviewBoardRepository.existsByReviewBoard(reviewBoardDTO.getRvno())) {
            redirectAttributes.addFlashAttribute("errorMessage", "리뷰가 이미 존재합니다.");
            return "redirect:/reservation/list";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        Optional<ReservationEntity> reservation = reservationRepository.findById(reviewBoardDTO.getRvno());

        if (reservation.isPresent()) {
            AccommodationEntity accommodation = reviewBoardService.findAccommodationByRoomId(reservation.get().getRoom_id().getRno());

            reviewBoardDTO.setWriterEmail(member.getEmail());
            reviewBoardDTO.setAno(accommodation.getAno());
            reviewBoardDTO.setRno(reservation.get().getRoom_id().getRno());
            reviewBoardDTO.setRvno(reservation.get().getRvno());
            Long rbno = reviewBoardService.register(reviewBoardDTO);
            images.forEach(i -> {
                imgService.ReviewBoardRegister(i,rbno);
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
        CustomUserDetails customUserDetails = new CustomUserDetails(memberRepository.save(member));
        SecurityContextHolder.getContext().setAuthentication
                (new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities()));
        return "redirect:/reservation/list";
    }
}
