package com.connectravel.controller;

import com.connectravel.domain.dto.*;
import com.connectravel.domain.entity.Member;
import com.connectravel.service.AdminBoardService;
import com.connectravel.service.ImgService;
import com.connectravel.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("adminboard")
@Log4j2
@RequiredArgsConstructor
public class AdminBoardController {

    private final AdminBoardService adminBoardService;
    private final ImgService imgService;
    private final MemberService memberService;

    @GetMapping("{category}")
    public String list(@PathVariable("category") String category, PageRequestDTO pageRequestDTO, Model model) {

        PageResultDTO<AdminBoardDTO, Object[]> pageResultDTO = adminBoardService.getPaginatedAdminBoardList(pageRequestDTO, category);
        if (pageResultDTO.getTotalPage() == 0) {
            pageResultDTO.setTotalPage(1);
        } // 글이 하나도 없을 땐 0으로 인식하므로

        model.addAttribute("result", pageResultDTO);
        model.addAttribute("category", category);
        return "adminboard/list";
    }

    @GetMapping("{category}/register")
    public String register(@PathVariable("category") String category, Model model) {
        model.addAttribute("category", category);

        return "adminboard/register";
    }

    @PostMapping("register")
    public String registerPost(@AuthenticationPrincipal Member member, Principal principal, AdminBoardDTO dto, RedirectAttributes redirectAttributes, Authentication authentication, @RequestParam("images") List<MultipartFile> images) {

        String username = principal.getName();
        MemberDTO memberDTO = memberService.getMember(member.getId());

        dto.setWriterEmail(member.getEmail());

        Long abno = adminBoardService.registerAdminBoard(dto); //새로 추가된 엔티티의 번호(dto)

        images.forEach(i -> {
            imgService.addAdminBoardImg(i, abno);
        });

        redirectAttributes.addFlashAttribute("msg", abno);

        return "redirect:/adminboard/" + dto.getCategory();
    }

    @GetMapping({"read", "modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long abno, Model model) {
        log.info("abno : " + abno);

        AdminBoardDTO adminBoardDTO = adminBoardService.getAdminBoard(abno);

        log.info(adminBoardDTO);

        List<ImgDTO> adminBoardImgDTOS = adminBoardService.getAdminBoardImgList(abno);

        System.out.println("image : " + adminBoardImgDTOS);
        String category = adminBoardDTO.getCategory();

        model.addAttribute("adminBoardImgs", adminBoardImgDTOS);
        model.addAttribute("dto", adminBoardDTO);
        model.addAttribute("category", category);
    }

    @PostMapping("modify")
    public String modify(AdminBoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
        log.info("post modify.....");
        log.info("dto : " + dto);

        adminBoardService.updateAdminBoard(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("abno", dto.getAbno());
        redirectAttributes.addAttribute("category", dto.getCategory());

        return "redirect:/adminboard/read";
    }

    @PostMapping("remove")
    public String remove(long abno, RedirectAttributes redirectAttributes) {

        adminBoardService.deleteAdminBoard(abno);

        redirectAttributes.addFlashAttribute("msg", abno);

        return "redirect:/adminboard/notice";
    }
} //class
