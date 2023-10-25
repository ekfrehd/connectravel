package org.ezone.room.controller;

import lombok.extern.log4j.Log4j2;
import org.ezone.room.security.CustomUserDetails;
import org.ezone.room.security.TokenProvider;
import org.ezone.room.constant.Role;
import org.ezone.room.dto.MemberFormDto;
import org.ezone.room.dto.ResponseDTO;
import org.ezone.room.entity.Member;
import org.ezone.room.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("member")
@Controller
@Log4j2
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager; // 스프링 시큐리티 로그인

    @GetMapping(value = "join")
    public String memberForm(HttpServletRequest request, @RequestParam(name = "error", required = false) String error, Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        if (error != null && error.equals("signup")) {
            model.addAttribute("errorMessage", "회원 가입이 필요합니다.");
        }

        return "member/join";
    }

    @PostMapping(value = "join")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model,
                             @RequestParam String tel1, @RequestParam String tel2, @RequestParam String tel3) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("memberFormDto", memberFormDto);
            return "redirect:/member/join";
        }

        String tel = tel1 + "-" + tel2 + "-" + tel3;
        memberFormDto.setTel(tel);

        try{
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";

        }
        return "redirect:/";
    }

    @GetMapping(value = "login")
    public String loginMember(Model model) {
        return "member/login";
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody MemberFormDto memberFormDto) throws Exception {
        Authentication authentication; // 스프링 시큐리티 로그인 객체 불러오기

        try { // 스프링 시큐리티를 이용한 로그인 확인.
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            memberFormDto.getEmail(),
                            memberFormDto.getPassword()
                    )
            );
        } catch (BadCredentialsException e) { // 틀렸을때 예외 처리
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setError("ID나 PASSWORD가 틀립니다.");
            return ResponseEntity.badRequest().body(responseDTO);
        } // 틀리면 걍 객체를 반환 시키는게 더 빠름 - ajax 처리.

        // 맞으면 인증, 권한 부여하기
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // token을 만들어서 발송하는 부분
        String token = tokenProvider.create(userDetails);
        MemberFormDto responseMemberFormDto = new MemberFormDto();
        responseMemberFormDto.setToken(token);

        return ResponseEntity.ok(responseMemberFormDto);
    }

    @GetMapping("memberinfo")
    public String memberInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        model.addAttribute("memberInfo", member);
        return "member/memberinfo";
    }

    @GetMapping(value = "update")
    public String memberInfoEdit(Model model, MemberFormDto memberFormDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        memberFormDto.setEmail(member.getEmail());
        memberFormDto.setName(member.getName());
        memberFormDto.setNickName(member.getNickName());

        // 전화번호 split
        String tel = member.getTel();
        String[] parts = tel.split("-");
        String tel1 = parts[0]; String tel2 = parts[1]; String tel3 = parts[2];
        memberFormDto.setTel1(tel1);
        memberFormDto.setTel2(tel2);
        memberFormDto.setTel3(tel3);

        model.addAttribute("memberFormDto", memberFormDto);

        return "member/update";

    }

    @PostMapping(value = "update")
    public String editProfile(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model,
                              @RequestParam String tel1, @RequestParam String tel2, @RequestParam String tel3,
                              RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            model.addAttribute("memberFormDto", memberFormDto);
            return "member/update";
        }
        String tel = tel1 + "-" + tel2 + "-" + tel3;
        memberFormDto.setTel(tel);

        if(memberFormDto.getEmail().equals("test100@test.com") || memberFormDto.getEmail().equals("test101@test.com")){
            redirectAttributes.addFlashAttribute("errorMessage", "테스트 계정은 프로필을 변경할 수 없습니다!!!!!!!!!!!!!!!!!!!!");
            return "redirect:/member/update";
        }

        // 세이브와 동시에 스프링 시큐리티 반영
        CustomUserDetails customUserDetails = new CustomUserDetails(memberService.editMember(memberFormDto));
        SecurityContextHolder.getContext().setAuthentication
                (new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities()));

        return "redirect:/member/memberinfo";
    }

    @GetMapping(value = "seller")
    public String CheckSeller(Model model, MemberFormDto memberFormDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        memberFormDto.setEmail(member.getEmail());
        memberFormDto.setName(member.getName());

        // 전화번호 split
        String tel = member.getTel();
        String[] parts = tel.split("-");
        String tel1 = parts[0]; String tel2 = parts[1]; String tel3 = parts[2];
        memberFormDto.setTel1(tel1);
        memberFormDto.setTel2(tel2);
        memberFormDto.setTel3(tel3);

        model.addAttribute("memberFormDto", memberFormDto);

        return "member/seller";
    }

    @PostMapping(value ="seller")
    public String CheckSeller(@RequestParam String tel1, @RequestParam String tel2, @RequestParam String tel3,
                              MemberFormDto memberFormDto) {

        String tel = tel1 + "-" + tel2 + "-" + tel3;
        memberFormDto.setTel(tel);
        memberFormDto.setName(memberFormDto.getName());
        memberFormDto.setRole(Role.SELLER);

        // 세이브와 동시에 스프링 시큐리티 반영
        CustomUserDetails customUserDetails = new CustomUserDetails(memberService.changeSeller(memberFormDto));
        SecurityContextHolder.getContext().setAuthentication
                (new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities()));

        return "redirect:/seller/accommodation/register";
    }

    @GetMapping(value = "changepw")
    public String changePassword(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }
        return "member/changepw";
    }

    @PostMapping(value = "changepw")
    public String chnagePassword(Model model, @ModelAttribute("currentPassword") String currentPassword,
                                 @ModelAttribute MemberFormDto memberFormDto,
                                 RedirectAttributes redirectAttributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        if(member.getEmail().equals("test100@test.com") || member.getEmail().equals("test101@test.com")){
            redirectAttributes.addFlashAttribute("errorMessage", "테스트 계정은 비밀번호을 변경할 수 없습니다!!!!!!!!!!!!!!!!!!!!");
            return "redirect:/member/changepw";
        }

        if(!passwordEncoder.matches(currentPassword, member.getPassword())){
            redirectAttributes.addFlashAttribute("errorMessage", "현재 비밀번호가 틀립니다.");
            return "redirect:/member/changepw";
        }

        memberFormDto.setEmail(member.getEmail());
        CustomUserDetails customUserDetails = new CustomUserDetails(memberService.changePassword(memberFormDto));
        SecurityContextHolder.getContext().setAuthentication
                (new UsernamePasswordAuthenticationToken(customUserDetails, customUserDetails.getPassword(), customUserDetails.getAuthorities()));

        return "redirect:/member/memberinfo";
    }

    @GetMapping(value = "/point")
    public String point(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        model.addAttribute("memberInfo", member);
        return "member/point";
    }
}