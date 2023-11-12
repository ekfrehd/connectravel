package com.connectravel.controller;

import com.connectravel.domain.entity.Member;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.security.service.MemberContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class SellerDashBoardController {

    private final AccommodationRepository accommodationRepository;

    @GetMapping("dashboard")
    public String main(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((MemberContext) authentication.getPrincipal()).getMember();

        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }
        if(!accommodationRepository.existsAccommodationByMemberId(member.getId())){
            return "redirect:/seller/accommodation/register";
        }

        return "dashboard/main";
    }

}