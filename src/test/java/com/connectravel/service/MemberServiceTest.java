package com.connectravel.service;

import com.connectravel.dto.MemberFormDto;
import com.connectravel.entity.Member;
import com.connectravel.repository.MemberRepository;
import org.ezone.room.constant.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    private Member testMember;
    private MemberFormDto testFormDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        testMember = new Member();
        testMember.setId("testId");
        testMember.setName("testName");
        testMember.setEmail("test@example.com");
        testMember.setNickName("testNick");
        testMember.setTel("010-1234-5678");
        testMember.setRole(Role.USER); // 예시 역할

        testFormDto = new MemberFormDto();
        testFormDto.setName("testName");
        testFormDto.setEmail("test@example.com");
        testFormDto.setNickName("testNick");
        testFormDto.setTel("010-1234-5678");
        testFormDto.setRole(Role.USER); // 예시 역할
    }

    @Test
    void saveMember() {
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        memberService.saveMember(testMember);

        verify(memberRepository).save(testMember);
    }

    @Test
    void editMember() {
        when(memberRepository.findByEmail(testFormDto.getEmail())).thenReturn(testMember);

        memberService.editMember(testFormDto);

        verify(memberRepository).findByEmail(testFormDto.getEmail());
        verify(memberRepository).save(testMember);
    }

    @Test
    void changeSeller() {
        when(memberRepository.findByEmail(testFormDto.getEmail())).thenReturn(testMember);

        memberService.changeSeller(testFormDto);

        verify(memberRepository).findByEmail(testFormDto.getEmail());
        verify(memberRepository).save(testMember);
    }
}
