package com.connectravel.controller;

import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.QnaBoardDTO;
import com.connectravel.entity.Member;
import com.connectravel.repository.MemberRepository;
import com.connectravel.service.QnaBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("qna")
@Log4j2
@RequiredArgsConstructor
public class QnaBoardController {

    private final QnaBoardService qnaBoardService;

    @Autowired
    private MemberRepository memberRepository; // 멤버 조회가 필요하므로 추가

    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model){
        log.info ("■■■■■■■■■■ QnA 게시글 리스트 출력");

        /*Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = null;
        if (principal instanceof CustomUserDetails) {
            member = ((CustomUserDetails) principal).getMember();
        }
        if(member == null){
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }*/

        PageResultDTO<QnaBoardDTO, Object[]> pageResultDTO = qnaBoardService.getList(pageRequestDTO);
        if(pageResultDTO.getTotalPage()==0){ pageResultDTO.setTotalPage(1);} // 글이 하나도 없을 땐 0으로 인식하므로

        model.addAttribute("result", pageResultDTO);
        return "qna/list";
    }

    @GetMapping("register")
    public void register(){
        log.info ("■■■■■■■■■■ QnA 게시글 등록 출력");
    }

    @PostMapping ("register")
    public String registerPost(@Valid QnaBoardDTO dto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Authentication authentication){
        log.info ("■■■■■■■■■■ QnA 게시글 등록 실행");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("alertMessage");
            return "redirect:/qna/register";
        }

        //Member member = memberRepository.findByEmail(authentication.getName());
        String email = "1111@naver.com";

        Member member = memberRepository.findByEmail(email);

        dto.setWriterEmail(member.getEmail());

        Long bno = qnaBoardService.register(dto); //새로 추가된 엔티티의 번호(dto)

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/qna/list";
    }


    @GetMapping({"read", "modify"})
    public void read(@ModelAttribute ("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model){
        log.info ("■■■■■■■■■■ QnA 게시글 조회 or 수정 출력");
        QnaBoardDTO qnaBoardDTO = qnaBoardService.get(bno);

        model.addAttribute("dto", qnaBoardDTO);
    }

    @PostMapping("modify")
    public String modify(QnaBoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){
        log.info ("■■■■■■■■■■ QnA 게시글 수정 실행");
        qnaBoardService.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", dto.getBno());

        return "redirect:/qna/read";
    }

    @PostMapping("remove")
    public String remove(long bno, RedirectAttributes redirectAttributes){
        log.info ("■■■■■■■■■■ QnA 게시글 삭제 실행");
        qnaBoardService.removeWithReplies(bno);

        redirectAttributes.addFlashAttribute("msg",bno);

        return "redirect:/qna/list";
    }
}
