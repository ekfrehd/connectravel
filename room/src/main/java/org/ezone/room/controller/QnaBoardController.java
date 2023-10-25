package org.ezone.room.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.security.CustomUserDetails;
import org.ezone.room.dto.PageResultDTO;
import org.ezone.room.dto.QnaBoardDTO;
import org.ezone.room.dto.PageRequestDTO;
import org.ezone.room.entity.Member;
import org.ezone.room.repository.MemberRepository;
import org.ezone.room.service.QnaBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("qna")
@Log4j2
@RequiredArgsConstructor
public class QnaBoardController {

    private final QnaBoardService qnaBoardService; //의존성 주입 - RequiredArgsConstructor어노테이션 필요

    @Autowired
    private MemberRepository memberRepository; // 멤버조회가 필요하므로 추가

    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = null;
        if (principal instanceof CustomUserDetails) {
            member = ((CustomUserDetails) principal).getMember();
        }
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        PageResultDTO<QnaBoardDTO, Object[]> pageResultDTO = qnaBoardService.getList(pageRequestDTO);
        if(pageResultDTO.getTotalPage()==0){ pageResultDTO.setTotalPage(1);} // 글이 하나도 없을 땐 0으로 인식하므로

        model.addAttribute("result", pageResultDTO);
        return "qna/list";
    }

    @GetMapping("register")
    public void register(){

    }

    @PostMapping("register")
    public String registerPost(QnaBoardDTO dto, RedirectAttributes redirectAttributes, Authentication authentication){

        Member member = memberRepository.findByEmail(authentication.getName());

        dto.setWriterEmail(member.getEmail());

        Long bno = qnaBoardService.register(dto); //새로 추가된 엔티티의 번호(dto)

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/qna/list";
    }


    @GetMapping({"read", "modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model){

        QnaBoardDTO qnaBoardDTO = qnaBoardService.get(bno);

        model.addAttribute("dto", qnaBoardDTO);
    }

    @PostMapping("remove")
    public String remove(long bno, RedirectAttributes redirectAttributes){
        qnaBoardService.removeWithReplies(bno);

        redirectAttributes.addFlashAttribute("msg",bno);

        return "redirect:/qna/list";
    }

    @PostMapping("modify")
    public String modify(QnaBoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){

        qnaBoardService.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", dto.getBno());

        return "redirect:/qna/read";
    }
}
