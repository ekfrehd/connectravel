package com.connectravel.controller;

import com.connectravel.constant.SportEnum;
import com.connectravel.domain.entity.Accommodation;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.Reservation;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ReservationRepository;
import com.connectravel.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/view/v1")
@RequiredArgsConstructor
@Transactional
public class TestController {

    private final MemberService userService;
    private final MemberRepository userRepository;
    private final ReservationRepository reservationRepository;

    @GetMapping("/crews/write")
    public String hello(Model model, @AuthenticationPrincipal Member member){
        List<SportEnum> sportEnums = List.of(SportEnum.values());

        List<Reservation> reservations = reservationRepository.findByMember_Username(member.getUsername());

        List<Accommodation> accommodations = reservations.stream()
                .map(reservation -> reservation.getRoom().getAccommodation())
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("accommodations", accommodations);

        model.addAttribute("sportEnums",sportEnums);
        return "crew/write";
    }


}
