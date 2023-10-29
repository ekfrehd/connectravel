package com.connectravel.service;

import com.connectravel.dto.MemberFormDTO;
import com.connectravel.entity.Member;
import com.connectravel.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원가입
    public Member saveMember(Member member) {
        return  memberRepository.save(member);
    }

    public Member editMember(MemberFormDTO memberFormDto){
        Member member = memberRepository.findByEmail(memberFormDto.getEmail());
        member.setTel(memberFormDto.getTel());
        member.setName(memberFormDto.getName());
        member.setNickName(memberFormDto.getNickName());
        memberRepository.save(member);
        return member;
    }


    public Member changeSeller(MemberFormDTO memberFormDto){
        Member member = memberRepository.findByEmail(memberFormDto.getEmail());
        member.setRole(memberFormDto.getRole());
        member.setName(memberFormDto.getName());
        member.setTel(memberFormDto.getTel());
        memberRepository.save(member);
        return member;
    }
}
