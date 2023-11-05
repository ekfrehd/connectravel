package com.connectravel.controller;

import com.connectravel.domain.dto.MemberDTO;
import com.connectravel.domain.dto.QnaReplyDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.service.MemberService;
import com.connectravel.service.QnaReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("qna")
@Log4j2
@RequiredArgsConstructor
public class QnaReplyController {

    private final QnaReplyService qnaReplyService; //의존성 자동주입
    private final MemberService memberService;

    @GetMapping(value = "qnareply/{bno}")
    public ResponseEntity<List<QnaReplyDTO>> getListByBoard(@PathVariable("bno") Long bno) {
        return new ResponseEntity<>(qnaReplyService.getQnaRepliesByQbno(bno), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Long> register(@AuthenticationPrincipal Member member, Principal principal, @RequestBody QnaReplyDTO qnaReplyDTO, Authentication authentication) {

        String username = principal.getName();
        MemberDTO memberDTO = memberService.getMember(member.getId());

       /* try {
            // Code to fetch Member entity using email
            // ...
        } catch (EntityNotFoundException ex) {
            log.error("Member not found for email: {}", member.getEmail());
            // Handle the exception (e.g., return an error response or redirect)
        }*/

        log.info(qnaReplyDTO);

        Long qrno = qnaReplyService.createQnaReply(qnaReplyDTO);

        return new ResponseEntity<>(qrno, HttpStatus.OK);
    }

    @DeleteMapping("{qrno}")
    public ResponseEntity<String> remove(@PathVariable("qrno") Long qrno) {
        log.info("RNO : " + qrno);

        qnaReplyService.deleteQnaReply(qrno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping("{qrno}")
    public ResponseEntity<String> modify(@RequestBody QnaReplyDTO qnaReplyDTO) {
        log.info(qnaReplyDTO);

        qnaReplyService.updateQnaReply(qnaReplyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
