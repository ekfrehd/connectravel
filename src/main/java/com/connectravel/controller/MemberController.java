package com.connectravel.controller;

import com.connectravel.domain.dto.MemberDTO;
import com.connectravel.domain.entity.Member;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    @Autowired
    private MemberService memberService;

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

        model.addAttribute("username", memberDTO);

        return "member/mypage";
    }



}