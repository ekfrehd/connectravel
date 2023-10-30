package com.connectravel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.connectravel.dto.*;
import com.connectravel.entity.Member;
import com.connectravel.repository.MemberRepository;
import com.connectravel.service.AdminBoardService;
import com.connectravel.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("adminboard")
@Log4j2
@RequiredArgsConstructor
public class AdminBoardController {

    private final AdminBoardService adminBoardService;

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("{category}")
    public String list(@PathVariable("category") String category,
            PageRequestDTO pageRequestDTO, Model model){

        PageResultDTO<AdminBoardDTO, Object[]> pageResultDTO = adminBoardService.getList(pageRequestDTO, category);
        if(pageResultDTO.getTotalPage()==0){ pageResultDTO.setTotalPage(1);} // 글이 하나도 없을 땐 0으로 인식하므로

        model.addAttribute("result", pageResultDTO);
        model.addAttribute("category", category);
        return "adminboard/list";
    }

    @GetMapping("{category}/register")
    public String register(@PathVariable("category") String category, Model model){
        model.addAttribute("category", category );

        return "adminboard/register";
    }

    @PostMapping("register")
    public String registerPost(AdminBoardDTO dto, RedirectAttributes redirectAttributes, Authentication authentication,
                               @RequestParam("images") List<MultipartFile> images){

        //Member member = memberRepository.findByEmail(authentication.getName());

        String email = "1111@naver.com";
        Member member = memberRepository.findByEmail(email);

        dto.setWriterEmail(member.getEmail());

        Long bno = adminBoardService.register(dto); //새로 추가된 엔티티의 번호(dto)

        /*images.forEach(i -> {
            imgService.AdminBoardRegister(i, bno);
        });*/

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/adminboard/"+dto.getCategory();
    }

    @GetMapping({"read", "modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model){
        log.info("bno : " + bno);

        AdminBoardDTO adminBoardDTO = adminBoardService.get(bno);

        log.info(adminBoardDTO);

        List<ImgDTO> adminBoardImgDTOS = adminBoardService.getImgList(bno);

        System.out.println("image : " + adminBoardImgDTOS);
        String category = adminBoardDTO.getCategory();

        model.addAttribute("adminBoardImgs", adminBoardImgDTOS);
        model.addAttribute("dto", adminBoardDTO);
        model.addAttribute("category", category);
    }

    @PostMapping("remove")
    public String remove(long bno, RedirectAttributes redirectAttributes){
        log.info("bno : " + bno);

        adminBoardService.removeWithReplies(bno);

        redirectAttributes.addFlashAttribute("msg",bno);

        return "redirect:/adminboard/list";
    }

    @PostMapping("modify")
    public String modify(AdminBoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){
        log.info("post modify.....");
        log.info("dto : " + dto);

        adminBoardService.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", dto.getBno());
        redirectAttributes.addAttribute("category", dto.getCategory());

        return "redirect:/adminboard/read";
    }
} //class
