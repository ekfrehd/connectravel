package com.connectravel.controller;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.dto.RoomDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ReservationRepository;
import com.connectravel.repository.ReviewBoardRepository;
import com.connectravel.repository.RoomRepository;
import com.connectravel.service.AccommodationService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Period;


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

    private final ReservationService reservationService;

    ModelMapper modelMapper = new ModelMapper();

    @GetMapping("register")
    public String register(@RequestParam("rno") Long rno, @RequestParam("startDate") LocalDate startDate,
                           @RequestParam("endDate") LocalDate endDate, Model model, @AuthenticationPrincipal Member member) {
        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        RoomDTO roomDTO = roomService.getRoom(rno);
        int price = roomDTO.getPrice();

        Period period = Period.between(startDate, endDate);
        int days = period.getDays();

        int totalPrice = price * days;
        roomDTO.setPrice(totalPrice);
        AccommodationDTO accommodationDTO = accommodationService.findByAno(roomDTO.getAccommodationDTO().getAno());

        model.addAttribute("days", days);
        model.addAttribute("acc", accommodationDTO);
        model.addAttribute("dto", roomDTO);
        model.addAttribute("member", member);

        return "reservation/register";
    }



    /*//사용자가 입력시 다시한번 검증
    @PostMapping("register")
    public String register(ReservationDTO rvDTO, RoomDTO roomDTO , Authentication authentication,
                           RedirectAttributes redirectAttributes) throws Exception{
        //rtts.addAttribute("StartDate",rvDTO.getStartDate().toString());
        //rtts.addAttribute("EndDate",rvDTO.getEndDate().toString());
        //사용자가 입력한 s,e date 가지고 redirect함--그러면 초기화안됨.

        //예약검사후 예약함.
        rvDTO.setMoney(roomDTO.getPrice());
        Long rvno = service.register(rvDTO,roomDTO,authentication);

        redirectAttributes.addFlashAttribute("rvno", rvno);
        return "redirect:/reservation/success";
    }*/


}

