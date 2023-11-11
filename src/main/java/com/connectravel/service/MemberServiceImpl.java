package com.connectravel.service;

import com.connectravel.dto.MemberDTO;
import com.connectravel.entity.Member;
import com.connectravel.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    // 기타 회원 서비스 관련 메서드들...

    @Override
    public MemberDTO entityToDTO(Member member) {
        if (member == null) {
            return null;
        }
        return MemberDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .tel(member.getTel())
                .point(member.getPoint())
                .role(member.getRole())
                .build();
    }

    @Override
    public Member dtoToEntity(MemberDTO memberDTO) {
        if (memberDTO == null) {
            return null;
        }
        return Member.builder()
                .id(memberDTO.getId())
                .name(memberDTO.getName())
                .nickName(memberDTO.getNickName())
                .email(memberDTO.getEmail())
                .tel(memberDTO.getTel())
                .point(memberDTO.getPoint())
                .role(memberDTO.getRole())
                .build();
    }
}
