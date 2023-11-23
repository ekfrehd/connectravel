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

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

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

    public MemberDTO getMember(String email) {

        Member member = memberRepository.findByEmail(email).orElse(new Member());
        ModelMapper modelMapper = new ModelMapper();
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);

        // 전화번호 split
        String tel = member.getTel();
        if (tel != null) {
            String[] parts = tel.split("-");
            if (parts.length == 3) {
                String tel1 = parts[0];
                String tel2 = parts[1];
                String tel3 = parts[2];
                memberDTO.setTel1(tel1);
                memberDTO.setTel2(tel2);
                memberDTO.setTel3(tel3);
            } else {
                // 적절한 처리를 수행 (예: 오류 메시지를 설정하거나 기본값을 지정)
            }
        } else {
            // 적절한 처리를 수행 (예: 오류 메시지를 설정하거나 기본값을 지정)
        }

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
    @Transactional
    public void updateMember(MemberDTO memberDTO) {

        Optional<Member> existingMember = memberRepository.findByEmail(memberDTO.getEmail());

        if (existingMember.isPresent()) {
            Member member = existingMember.get();

            // 사용자 정보 업데이트
            member.setEmail(memberDTO.getEmail());
            member.setUsername(memberDTO.getUsername());
            member.setNickName(memberDTO.getNickName());
            member.setTel(memberDTO.getTel1() + "-" + memberDTO.getTel2() + "-" + memberDTO.getTel3());

            // 패스워드가 제공된 경우에만 업데이트
            if (memberDTO.getPassword() != null) {
                member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
            }

            // 역할이 제공된 경우에만 업데이트
            if (memberDTO.getMemberRoles() != null) {
                Set<Role> roles = new HashSet<>();
                memberDTO.getMemberRoles().forEach(role -> {
                    Role r = roleRepository.findByRoleName(String.valueOf(role));
                    roles.add(r);
                });

                member.setMemberRoles(roles);
            }

            // 업데이트된 멤버를 저장
            memberRepository.save(member);
        } else {
            log.error("이메일로 회원을 찾을 수 없습니다: {}", memberDTO.getEmail());
        }
    }

    @Override
    public Member updateSeller(MemberDTO memberDTO) {

        Optional<Member> optionalMember = memberRepository.findByEmail(memberDTO.getEmail());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            member.setUsername(memberDTO.getUsername());
            member.setTel(memberDTO.getTel1() + "-" + memberDTO.getTel2() + "-" + memberDTO.getTel3());

            Role role = roleRepository.findByRoleName("ROLE_SELLER");
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            member.setMemberRoles(roles);

            memberRepository.save(member);

            return member;
        }
        else {
            return null;
        }
    }

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

    @Override
    public Member dtoToEntity(MemberDTO memberDTO) {

        if (memberDTO == null) {
            return null;
        }

        Member member = new Member();
        member.setId(memberDTO.getId());
        member.setUsername(memberDTO.getUsername());
        member.setNickName(memberDTO.getNickName());
        member.setEmail(memberDTO.getEmail());
        member.setTel(memberDTO.getTel());
        member.setPoint(memberDTO.getPoint());

        // Set<String>에서 Set<Role>로 변환
        Set<Role> roles = Optional.ofNullable(memberDTO.getMemberRoles())
                .orElse(Collections.emptySet())
                .stream()
                .map(roleName -> roleRepository.findByRoleName(roleName))
                .collect(Collectors.toSet());

        member.setMemberRoles(roles);

        return member;
    }

}