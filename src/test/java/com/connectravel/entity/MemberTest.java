package com.connectravel.entity;

import com.connectravel.constant.Role;
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
        member.setName("admin");
        member.setEmail("admin@example.com");
        member.setRole(Role.ADMIN);
        member.setPassword("password");
    }

    @Test
    public void testMemberCreation() {
        assertEquals("admin", member.getName());
        assertEquals("admin@example.com", member.getEmail());
        log.debug("Member created: {}", member);
    }
}
