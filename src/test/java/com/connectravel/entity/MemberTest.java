package com.connectravel.entity;

import com.connectravel.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {

    private static final Logger log = LoggerFactory.getLogger(MemberTest.class);
    private Member member;

    @BeforeEach
    public void setUp() {
        member = new Member();
        member.setName("John");
        member.setEmail("john@example.com");
        member.setPassword("password");
    }

    @Test
    public void testMemberCreation() {
        assertEquals("John", member.getName());
        assertEquals("john@example.com", member.getEmail());
        log.debug("Member created: {}", member);
    }
}
