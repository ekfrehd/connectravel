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
import org.springframework.web.bind.annotation.*;
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
    private PasswordEncoder passwordEncoder;;

    @GetMapping(value="/join")
    public String createMember() throws Exception {
        return "member/join";
    }

    @PostMapping(value="/join")
    public String createMember(MemberDTO memberDTO) throws Exception {

        ModelMapper modelMapper = new ModelMapper();

        Member member = modelMapper.map(memberDTO, Member.class);
        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));

        memberService.createMember(member);

        return "redirect:login";
    }

    @GetMapping("/mypage")
    public String getMember(@AuthenticationPrincipal Member member) { return "member/mypage"; }

    @GetMapping(value = "update")
    public String getMember(@AuthenticationPrincipal Member member, Model model) throws Exception {

        if (member == null) {
            return "redirect:/member/login";
        }

        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setEmail(member.getEmail());
        memberDTO.setUsername(member.getUsername());
        memberDTO.setNickName(member.getNickName());

        // 전화번호 split
        String tel = member.getTel();
        if (tel != null) {
            String[] parts = tel.split("-");
            if (parts.length == 3) {
                String tel1 = parts[0];
                String tel2 = parts[1];
                String tel3 = parts[2];
                memberDTO.setTel1(tel1);
                memberDTO.setTel2(tel2);
                memberDTO.setTel3(tel3);
            } else {
                // 적절한 처리를 수행 (예: 오류 메시지를 설정하거나 기본값을 지정)
            }
        } else {
            // 적절한 처리를 수행 (예: 오류 메시지를 설정하거나 기본값을 지정)
        }

        model.addAttribute("memberDTO", memberDTO);

        return "member/update";
    }

    @PostMapping(value = "update")
    public String updateMember(@AuthenticationPrincipal Member member, MemberDTO memberDTO,
                               RedirectAttributes redirectAttributes, Model model) {

        if (member == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member not found");
            return "redirect:/error";
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

        return "redirect:/member/mypage";
    }


    @GetMapping(value = "seller")
    public String updateSeller(@AuthenticationPrincipal Member member, Model model) {

        MemberDTO memberDTO = memberService.getMember(member.getId());

        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        memberDTO.setEmail(member.getEmail());
        memberDTO.setUsername(member.getUsername());

        // 전화번호 split
        String tel = member.getTel();
        if (tel != null) {
            String[] parts = tel.split("-");
            if (parts.length == 3) {
                String tel1 = parts[0];
                String tel2 = parts[1];
                String tel3 = parts[2];
                memberDTO.setTel1(tel1);
                memberDTO.setTel2(tel2);
                memberDTO.setTel3(tel3);
            } else {
                // 적절한 처리를 수행 (예: 오류 메시지를 설정하거나 기본값을 지정)
            }
        } else {
            // 적절한 처리를 수행 (예: 오류 메시지를 설정하거나 기본값을 지정)
        }
        model.addAttribute("memberDTO", memberDTO);

        return "member/seller";
    }

    @PostMapping(value ="seller")
    public String updateSeller(@AuthenticationPrincipal Member member,
                              @RequestParam String tel1,
                              @RequestParam String tel2,
                              @RequestParam String tel3) {

        MemberDTO memberDTO = memberService.getMember(member.getId());

        String tel = tel1 + "-" + tel2 + "-" + tel3;
        memberDTO.setTel(tel);

        Member updatedMember = memberService.updateSeller(memberDTO);

        return "redirect:/seller/list";
    }

    @GetMapping(value = "changepw")
    public String updatePassword(@AuthenticationPrincipal Member member, Model model) {

        MemberDTO memberDTO = memberService.getMember(member.getId());

        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        return "member/changepw";
    }

    @PostMapping(value = "changepw")
    public String updatePassword(@AuthenticationPrincipal Member member,
                                 @ModelAttribute("currentPassword") String currentPassword, @ModelAttribute("newPassword") String newPassword,
                                 RedirectAttributes redirectAttributes, Model model) {

        MemberDTO memberDTO = memberService.getMember(member.getId());

        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "현재 비밀번호가 틀립니다.");

            return "redirect:/member/changepw";
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