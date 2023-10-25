package org.ezone.room.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.security.CustomUserDetails;
import org.ezone.room.entity.Member;
import org.ezone.room.repository.AccommodationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("seller")
public class SellerDashBoardController {
    // seller 메인밑 sales 관리하면 될 듯?

    private final AccommodationRepository accommodationRepository;

    @GetMapping("dashboard")
    public String main(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }
        if(!accommodationRepository.existsAccommodationByMemberId(member.getId())){
            return "redirect:/seller/accommodation/register"; // 존재하지않으면 작성유도
        }
        return "dashboard/main";
    }
}
