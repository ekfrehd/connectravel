package com.connectravel.entity;

import com.connectravel.constant.Role;
import com.connectravel.dto.MemberFormDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Member {

    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name="system_uuid", strategy = "uuid")
    @Column(name = "id")
    private String id; // 아이디

    private String name; // 회원 이름

    @Column(unique = true)
    private String email; //회원 이메일

    @Column(scale = 0)
    private int point; // 포인트

    private String nickName; // 회원 닉네임

    private String tel; // 전화번호

    private String password; // 비밀번호

    @Enumerated(EnumType.STRING)
    private Role role; //회원 권한

    private String authProvider; // 인증 제공자 (카카오톡, 깃허브)

    public static Member createMember(MemberFormDTO memberFormDto){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setNickName(memberFormDto.getNickName());
        member.setTel(memberFormDto.getTel());
        member.setPassword(memberFormDto.getPassword());
        member.setRole(Role.USER); // 회원가입은 기본 유저
        return member;
    }
}
