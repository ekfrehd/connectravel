package com.connectravel.controller;

import com.connectravel.constant.SportEnum;
import com.connectravel.repository.MemberRepository;
import com.connectravel.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/view/v1")
@RequiredArgsConstructor
@Transactional
public class TestController {

    private final MemberService userService;
    private final MemberRepository userRepository;

    @GetMapping("/crews/write")
    public String hello(Model model){
        List<SportEnum> sportEnums = List.of(SportEnum.values());
        model.addAttribute("sportEnums",sportEnums);
        return "crew/write";
    }


}
