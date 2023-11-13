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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final MemberService memberService;
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @GetMapping(value = "/seller")
    public String showSellerForm(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        String email = principal.getName();
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            model.addAttribute("errorMessage", "회원 정보를 찾을 수 없습니다.");
            return "redirect:/member/login";
        }

        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
        model.addAttribute("memberDTO", memberDTO);

        return "member/seller";
    }

    @PostMapping(value = "/seller")
    public String changeSellerToROLE_SELLER(Principal principal) {
        // 현재 로그인한 사용자의 이메일을 가져옴
        String email = principal.getName();

        // 이메일을 사용하여 사용자의 권한을 SELLER로 변경
        Member updatedMember = memberService.changeSellerByEmail(email);

        // 변경된 Member 정보로 CustomUserDetails 생성
        if (updatedMember != null) {
            // SecurityContext에 새로운 Authentication 정보를 설정
            UserDetails userDetails = User.withUsername(updatedMember.getUsername())
                    .password(updatedMember.getPassword())
                    .authorities(updatedMember.getAuthorities())
                    .build();

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                    )
            );

        }

        // 숙박 시설 등록 페이지로 리디렉션
        return "redirect:/seller/accommodation/register";
    }


}