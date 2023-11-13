package com.connectravel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller/room")
@RequiredArgsConstructor
@Log4j2
public class SellerController {

    @GetMapping("/list")
    public String list() {
        // 여기에 판매자 리스트 페이지를 반환하는 로직을 구현
        return "seller/list"; // Thymeleaf 템플릿 또는 뷰 이름
    }
}