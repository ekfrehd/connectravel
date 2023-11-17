package com.connectravel.controller;

import com.connectravel.domain.dto.MemberDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.RoleRepository;
import com.connectravel.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

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

    @GetMapping(value="/mypage")
    @PreAuthorize("isAuthenticated() and (#member.username == principal.username)")
    public String myPage(@AuthenticationPrincipal Member member, Principal principal, Model model) throws Exception {

        String username = principal.getName();
        MemberDTO memberDTO = memberService.getMember(member.getId());

        if(memberDTO == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        model.addAttribute("username", memberDTO);

        return "member/mypage";
    }

    @GetMapping(value = "update")
    @PreAuthorize("isAuthenticated() and (#member.username == principal.username)")
    public String mypageUpdate(@AuthenticationPrincipal Member member, Principal principal, Model model) {

        String username = principal.getName();
        MemberDTO memberDTO = memberService.getMember(member.getId());

        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        memberDTO.setEmail(member.getEmail());
        memberDTO.setUsername(member.getUsername());
        memberDTO.setNickName(member.getNickName());

        if (member != null) {
            String tel = member.getTel();
            if (tel != null) {
                String[] parts = tel.split("-");
                if (parts.length == 3) {
                    memberDTO.setTel1(parts[0]);
                    memberDTO.setTel2(parts[1]);
                    memberDTO.setTel3(parts[2]);
                } else {
                }
            }
        }

        model.addAttribute("memberDTO", memberDTO);

        return "member/update";
    }

    @PostMapping(value = "update")
    public String editProfile(@AuthenticationPrincipal Member member, Principal principal,
                              @Valid MemberDTO memberDTO, BindingResult bindingResult,
                              @RequestParam String tel1, @RequestParam String tel2, @RequestParam String tel3,
                              RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("memberDTO", memberDTO);
            return "member/update";
        }

        String tel = tel1 + "-" + tel2 + "-" + tel3;
        memberDTO.setTel(tel);

        memberService.editMember(memberDTO);

        return "redirect:/member/mypage";
    }

    @GetMapping(value = "seller")
    public String CheckSeller(@AuthenticationPrincipal Member member, Principal principal, Model model) {

        String username = principal.getName();
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
                // 예외 처리: 올바르지 않은 전화번호 형식
            }
        } else {
            // 예외 처리: 전화번호가 null인 경우
        }

        model.addAttribute("memberDTO", memberDTO);

        return "member/seller";
    }

    @PostMapping(value ="seller")
    public String CheckSeller(@AuthenticationPrincipal Member member,
                              @RequestParam String tel1,
                              @RequestParam String tel2,
                              @RequestParam String tel3) {

        MemberDTO memberDTO = memberService.getMember(member.getId());

        String tel = tel1 + "-" + tel2 + "-" + tel3;
        memberDTO.setTel(tel);

        Member updatedMember = memberService.changeSeller(memberDTO);



        return "redirect:/seller/list";
    }

    @GetMapping(value = "changepw")
    public String changePassword(@AuthenticationPrincipal Member member, Principal principal, Model model) {

        String username = principal.getName();
        MemberDTO memberDTO = memberService.getMember(member.getId());

        if (member == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        return "member/changepw";
    }

    @PostMapping(value = "changepw")
    public String changePassword(@AuthenticationPrincipal Member member, Principal principal,
                                 Model model, @ModelAttribute("currentPassword") String currentPassword,
                                 @ModelAttribute("newPassword") String newPassword,
                                 RedirectAttributes redirectAttributes) {

        String username = principal.getName();
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

        return "redirect:/member/mypage";
    }

    @GetMapping(value = "/point")
    public String point(@AuthenticationPrincipal Member member, Principal principal, Model model) {

        String username = principal.getName();
        MemberDTO memberDTO = memberService.getMember(member.getId());

        model.addAttribute("mypage", member);
        return "member/point";
    }

}