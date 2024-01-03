//package org.ezone.room.controller;
//
//import lombok.extern.log4j.Log4j2;
//import org.ezone.room.security.CustomUserDetails;
////import org.ezone.room.security.TokenProvider;
//import org.ezone.room.constant.Role;
////import org.ezone.room.dto.MemberFormDto;
////import org.ezone.room.dto.ResponseDTO;
//import org.ezone.room.entity.Member;
////import org.ezone.room.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//
//@RequestMapping("member")
//@Controller
//@Log4j2
//@RequiredArgsConstructor
//public class MemberController {
//
//    @GetMapping(value = "logintest")
//    public String loginMember() {
//        return "member/logintest";
//    }
//
//
//
//}
