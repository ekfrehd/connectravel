package com.connectravel.controller;

import com.connectravel.domain.dto.*;
import com.connectravel.domain.entity.Member;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ReservationRepository;
import com.connectravel.repository.ReviewBoardRepository;
import com.connectravel.repository.RoomRepository;
import com.connectravel.service.AccommodationService;
import com.connectravel.service.MemberService;
import com.connectravel.service.ReservationService;
import com.connectravel.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("reservation")
@RequiredArgsConstructor
@Log4j2
@EnableJpaAuditing
public class ReservationController {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final ReviewBoardRepository reviewBoardRepository;
    private final MemberRepository memberRepository;
    private final AccommodationService accommodationService;
    private final RoomService roomService;
    private final MemberService memberService;
    private final ReservationService reservationService;

    ModelMapper modelMapper = new ModelMapper();

    @GetMapping("register")
    public String register(@RequestParam("rno") Long rno,
                           @RequestParam("startDate") String startDateStr,
                           @RequestParam("endDate") String endDateStr,
                           Model model,
                           @AuthenticationPrincipal Member member) {
        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        RoomDTO roomDTO = roomService.getRoom(rno);
        int price = roomDTO.getPrice();

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        Period period = Period.between(startDate, endDate);
        int days = period.getDays();


        int totalPrice = price * days;
        roomDTO.setPrice(totalPrice);
        AccommodationDTO accommodationDTO = accommodationService.findByAno(roomDTO.getAccommodationDTO().getAno());

        ReservationDTO rv = new ReservationDTO();
        rv.setStartDate(startDate);
        rv.setEndDate(endDate);

        model.addAttribute("days", days);
        model.addAttribute("acc", accommodationDTO);
        model.addAttribute("dto", roomDTO);
        model.addAttribute("rv", rv);
        model.addAttribute("member", member);

        return "reservation/register";
    }


    @PostMapping("register")
    public String register(ReservationDTO rvDTO, @RequestParam("rno") Long rno,
                           @AuthenticationPrincipal Member member,
                           RedirectAttributes redirectAttributes) {

        // 사용자가 로그인하지 않은 경우 에러 메시지를 추가하고 로그인 페이지로 리다이렉트
        if (member == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        // RoomDTO와 MemberDTO를 설정
        RoomDTO roomDTO = roomService.getRoom(rno);
        MemberDTO memberDTO = memberService.entityToDTO(member);
        rvDTO.setRoomDTO(roomDTO);
        rvDTO.setMemberDTO(memberDTO);

        // 예약 서비스를 호출하여 예약 진행
        ReservationDTO savedRvDTO = reservationService.registerReservation(rvDTO);

        // 예약 번호와 성공 메시지를 리다이렉트 속성에 추가
        redirectAttributes.addFlashAttribute("rvno", savedRvDTO.getRvno());
        redirectAttributes.addFlashAttribute("message", "예약이 성공적으로 완료되었습니다.");

        return "redirect:/reservation/success";
    }

    @GetMapping("success")
    public void success(Model model) {

    }

    @GetMapping("list")
    public String list(Model model, @AuthenticationPrincipal Member member) {

        // 사용자가 로그인하지 않은 경우 에러 메시지를 추가하고 로그인 페이지로 리다이렉트
        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        List<ReservationDTO> reservations = reservationService.listUserRoomBookings(member.getUsername());

        List<AccommodationDTO> accommodationList = new ArrayList<>();
        for (ReservationDTO reservation : reservations) {
            RoomDTO roomDTO = roomService.getRoom(reservation.getRoomDTO().getRno());
            AccommodationDTO accommodationDTO = accommodationService.findByAno(roomDTO.getAccommodationDTO().getAno());
            accommodationList.add(accommodationDTO);
        }

        model.addAttribute("reservations", reservations);
        model.addAttribute("accommodationList", accommodationList);
        model.addAttribute("today", LocalDate.now());

        return "reservation/list";
    }

    @GetMapping("cancel")
    public String cancel(Long rvno, @AuthenticationPrincipal Member member, RedirectAttributes rtts){

        rtts.addAttribute("IsCancel",reservationService.requestCancel(rvno, member.getEmail()));
        return "redirect:/reservation/list";
    }

}