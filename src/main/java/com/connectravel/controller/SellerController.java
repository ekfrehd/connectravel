package com.connectravel.controller;

import com.connectravel.domain.dto.MemberDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("seller")
public class SellerController {

    @GetMapping("/list")
    //@PreAuthorize("hasRole('ROLE_SELLER')")
    public void list(Model model, MemberDTO member){

    }

}