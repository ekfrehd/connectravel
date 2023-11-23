package com.connectravel.controller;

import com.connectravel.domain.dto.MemberDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.repository.MemberRepository;
import com.connectravel.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value="/join")
    public String createMember() throws Exception { return "member/join"; }

    @PostMapping(value="/join")
    public String createMember(MemberDTO memberDTO) throws Exception {

        ModelMapper modelMapper = new ModelMapper();

        Member member = modelMapper.map(memberDTO, Member.class);
        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));

        memberService.createMember(member);

        return "redirect:login";
    }

    @GetMapping("/myPage")
    public String getMember(@AuthenticationPrincipal Member member) { return "member/myPage"; }

    @GetMapping(value = "update")
    public String getMember(@AuthenticationPrincipal Member member, Model model) throws Exception {

        if (member == null) {
            return "redirect:login";
        }

        MemberDTO memberDTO = memberService.getMember(member.getEmail());

        model.addAttribute("memberDTO", memberDTO);

        return "member/update";
    }

    @PostMapping(value = "update")
    public String updateMember(@AuthenticationPrincipal Member member, MemberDTO memberDTO,
                               RedirectAttributes redirectAttributes, Model model) {

        if (member == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found");
            return "redirect:login";
        }

        // 사용자 정보 업데이트
        member.setEmail(memberDTO.getEmail());
        member.setUsername(memberDTO.getUsername());
        member.setNickName(memberDTO.getNickName());
        member.setTel(memberDTO.getTel1() + "-" + memberDTO.getTel2() + "-" + memberDTO.getTel3());

        // 데이터베이스에 변경된 정보 저장
        memberRepository.save(member);

        // 변경된 정보로 다시 인증 토큰을 생성하여 설정합니다.
        UserDetails userDetails = userDetailsService.loadUserByUsername(memberDTO.getEmail());

        return "redirect:/member/myPage";
    }

    @GetMapping(value = "seller")
    public String updateSeller(@AuthenticationPrincipal Member member, Model model) {

        if (member == null) {
            return "redirect:/member/login";
        }

        // 현재 권한 정보를 DTO로 변환하여 모델에 추가
        MemberDTO memberDTO = memberService.getMember(member.getEmail());

        model.addAttribute("memberDTO", memberDTO);

        return "member/seller";
    }

    @PostMapping(value ="seller")
    public String updateSeller(@AuthenticationPrincipal Member member, MemberDTO memberDTO,
                               RedirectAttributes redirectAttributes, Model model) {

        if (member == null) {
            return "redirect:/member/login";
        }

        memberService.updateSeller(memberDTO);

        UserDetails userDetails = userDetailsService.loadUserByUsername(memberDTO.getEmail());

        return "redirect:/seller/list";
    }

    @GetMapping(value = "/updatePassword")
    public String updatePassword(@AuthenticationPrincipal Member member, Model model) {

        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        return "member/updatePassword";
    }

    @PostMapping(value = "/updatePassword")
    public String updatePassword(@AuthenticationPrincipal Member member, Model model, RedirectAttributes redirectAttributes,
                                 @ModelAttribute("currentPassword") String currentPassword, @ModelAttribute("newPassword") String newPassword) {

        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "현재 비밀번호가 틀립니다.");

            return "redirect:/member/updatePassword";
        }

        // 비밀번호 변경 로직이 수행된 후 Member 엔터티를 저장
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

        return "redirect:/member/login";
    }

    @GetMapping(value = "/point")
    public String point(@AuthenticationPrincipal Member member, Model model) {

        MemberDTO memberDTO = memberService.getMember(member.getId());

        model.addAttribute("member", member);

        return "member/point";
    }

}