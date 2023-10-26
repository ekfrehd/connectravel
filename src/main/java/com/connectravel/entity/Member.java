package com.connectravel.entity;

import com.connectravel.constant.Role;
import com.connectravel.dto.MemberFormDTO;
import lombok.*;
// import org.connectravel.dto.MemberFormDto;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "id")}, name = "member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name="system_uuid", strategy = "uuid")
    @Column(name = "id")
    private String id; // 아이디

    private String name;

    @Column(unique = true)
    private String email;

    @Column(scale = 0)
    private int point; // 포인트

    private String nickName; // 닉네임

    private String tel; // 전화번호

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String authProvider; // 인증 제공자 (카카오톡, 깃허브)

    public static Member createMember(MemberFormDTO memberFormDto, PasswordEncoder passwordEncoder){

        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setNickName(memberFormDto.getNickName());
        member.setTel(memberFormDto.getTel());
        String password = passwordEncoder.encode(memberFormDto.getPassword()); // 비밀번호 암호화
        member.setPassword(password);
        member.setRole(Role.USER); // 회원가입은 기본 유저
        return member;
    }

}
