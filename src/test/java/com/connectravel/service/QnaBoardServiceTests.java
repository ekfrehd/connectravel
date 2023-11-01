package com.connectravel.service;

import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.QnaBoardRepository;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class QnaBoardServiceTests {

    @Autowired
    private QnaBoardRepository qnaBoardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QnaBoardService qnaBoardService;

    @Test // 게시글 삭제 테스트
    public void testDelete () {
        qnaBoardService.removeWithReplies (21L);
    }
}










