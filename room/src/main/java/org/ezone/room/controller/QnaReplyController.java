package org.ezone.room.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.dto.QnaReplyDTO;
import org.ezone.room.entity.Member;
import org.ezone.room.repository.MemberRepository;
import org.ezone.room.service.QnaReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("qna")
@Log4j2
@RequiredArgsConstructor
public class QnaReplyController {

    @Autowired
    private MemberRepository memberRepository; // Mmemer 조회가 필요하므로 추가

    private final QnaReplyService qnaReplyService; //의존성 자동주입

    @GetMapping(value = "qnareply/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QnaReplyDTO>> getListByBoard(@PathVariable("bno") Long bno){

        return new ResponseEntity<>(qnaReplyService.getList(bno), HttpStatus.OK);

    }

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody QnaReplyDTO qnaReplyDTO, Authentication authentication){

        // authentication.getName() : 로그인한 id나 email을 불러온다. (String 형식)
        // 단 null(로그인 안한 경우)이면, NullPointException 오류가 발생하니 주의
        Member member = memberRepository.findByEmail(authentication.getName());

        qnaReplyDTO.setReplyer(member.getEmail());

        log.info(qnaReplyDTO);

        Long rno = qnaReplyService.register(qnaReplyDTO);

        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    @DeleteMapping("{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno){
        log.info("RNO : " + rno);

        qnaReplyService.remove(rno);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping("{rno}")
    public ResponseEntity<String> modify(@RequestBody QnaReplyDTO qnaReplyDTO){
        log.info(qnaReplyDTO);

        qnaReplyService.modify(qnaReplyDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
