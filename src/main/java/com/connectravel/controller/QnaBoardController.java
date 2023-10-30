package com.connectravel.controller;

import com.connectravel.domain.dto.MemberDTO;
import com.connectravel.domain.dto.PageRequestDTO;
import com.connectravel.domain.dto.PageResultDTO;
import com.connectravel.domain.dto.QnaBoardDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.service.MemberService;
import com.connectravel.service.QnaBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("qna")
@Log4j2
@RequiredArgsConstructor
public class QnaBoardController {

    private final QnaBoardService qnaBoardService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("■■■■■■■■■■ QnA 게시글 리스트 출력");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = null;
        /*if (principal instanceof CustomUserDetails) {
            member = ((CustomUserDetails) principal).getMember();
        }
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }*/

        PageResultDTO<QnaBoardDTO, Object[]> pageResultDTO = qnaBoardService.getPaginatedQnas(pageRequestDTO);
        if (pageResultDTO.getTotalPage() == 0) {
            pageResultDTO.setTotalPage(1);
        } // 글이 하나도 없을 땐 0으로 인식하므로

        model.addAttribute("result", pageResultDTO);
        return "qna/list";
    }

    @GetMapping("register")
    public void register() {
        log.info("■■■■■■■■■■ QnA 게시글 등록 출력");
    }

    @PostMapping("register")
    public String registerPost(@AuthenticationPrincipal Member member, Principal principal, @Valid QnaBoardDTO dto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Authentication authentication) {
        log.info("■■■■■■■■■■ QnA 게시글 등록 실행");

        String username = principal.getName();
        MemberDTO memberDTO = memberService.getMember(member.getId());

        log.info("username" + username);
        log.info("memberDTO" + memberDTO.getId());
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("alertMessage");
            return "redirect:/qna/register";
        }

        dto.setWriterEmail(member.getEmail());
        dto.setWriterName(member.getUsername());

        Long qbno = qnaBoardService.createQna(dto); //새로 추가된 엔티티의 번호(dto) // 게시글 등록
        redirectAttributes.addFlashAttribute("msg", qbno);

        return "redirect:/qna/list";
    }


    @GetMapping({"read", "modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long qbno, Model model) {
        log.info("■■■■■■■■■■ QnA 게시글 조회 or 수정 출력");

        QnaBoardDTO qnaBoardDTO = qnaBoardService.getQnaByQbno(qbno);

        model.addAttribute("dto", qnaBoardDTO);
    }

    @PostMapping("modify")
    public String modify(QnaBoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
        log.info("■■■■■■■■■■ QnA 게시글 수정 실행");
        qnaBoardService.updateQna(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("qbno", dto.getQbno());

        return "redirect:/qna/read";
    }

    @PostMapping("remove")
    public String remove(long qbno, RedirectAttributes redirectAttributes) {
        log.info("■■■■■■■■■■ QnA 게시글 삭제 실행");
        qnaBoardService.deleteQnaWithReplies(qbno);

        redirectAttributes.addFlashAttribute("msg", qbno);

        return "redirect:/qna/list";
    }
}
