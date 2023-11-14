package com.connectravel.repository;

import com.connectravel.domain.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void findByEmailTest() {
        String testEmail = "seller@test.com"; // 데이터베이스에 존재하는 테스트용 이메일 주소
        Member member = memberRepository.findByEmail(testEmail);
        assertNotNull(member, "Member should not be null for existing email");
        assertEquals(testEmail, member.getEmail(), "Email should match the test email");
    }
}
