package com.connectravel.service.impl;

import com.connectravel.domain.dto.MemberDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.Role;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.RoleRepository;
import com.connectravel.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberDTO entityToDTO(Member member) {

        if (member == null) {
            return null;
        }

        Set<String> roles = member.getMemberRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        return MemberDTO.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .tel(member.getTel())
                .point(member.getPoint())
                .memberRoles(roles)
                .build();
    }

    @Transactional
    @Override
    public void createMember(Member member) {

        Role role = roleRepository.findByRoleName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        member.setMemberRoles(roles);

        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void modifyMember(MemberDTO memberDTO) {

        ModelMapper modelMapper = new ModelMapper();
        Member account = modelMapper.map(memberDTO, Member.class);

        account.setEmail(memberDTO.getEmail());
        account.setNickName(memberDTO.getNickName());
        account.setTel(memberDTO.getTel());

        if (memberDTO.getMemberRoles() != null) {
            Set<Role> roles = new HashSet<>();
            memberDTO.getMemberRoles().forEach(role -> {
                Role r = roleRepository.findByRoleName(String.valueOf(role));
                roles.add(r);
            });
            account.setMemberRoles(roles);
        }

        account.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        memberRepository.save(account);

    }

    @Transactional
    public MemberDTO getMember(Long id) {

        Member member = memberRepository.findById(id).orElse(new Member());
        ModelMapper modelMapper = new ModelMapper();
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);

        Set<String> roles = member.getMemberRoles()
                .stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toSet());

        memberDTO.setMemberRoles(roles);

        return memberDTO;
    }

    @Transactional
    public MemberDTO getMember(String email) {

        Member member = memberRepository.findByEmail(email).orElse(new Member());
        ModelMapper modelMapper = new ModelMapper();
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);

        Set<String> roles = member.getMemberRoles()
                .stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toSet());

        memberDTO.setMemberRoles(roles);

        return memberDTO;
    }

    @Transactional
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    @Override
    public void deleteMember(Long id) { memberRepository.deleteById(id); }

    @Override
    public Member updateSeller(MemberDTO memberDTO) {

        Member member = memberRepository.findByEmail(memberDTO.getEmail()).orElse(new Member());
        ModelMapper modelMapper = new ModelMapper();
        Member account = modelMapper.map(memberDTO, Member.class);

        account.setMemberRoles(Collections.singleton(roleRepository.findByRoleName("ROLE_SELLER")));
        account.setUsername(memberDTO.getUsername());
        account.setEmail(memberDTO.getEmail());
        account.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        account.setTel(memberDTO.getTel());

        memberRepository.save(account);

        return account;
    }

}