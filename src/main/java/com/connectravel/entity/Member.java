package com.connectravel.entity;

import lombok.*;
import com.connectravel.constant.Role;
import com.connectravel.dto.MemberFormDTO;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "id")}, name = "member")
public class Member {

    @Id // PK 지정
    @GeneratedValue(generator = "system_uuid") // 자동 일련번호 생성 전략
    @GenericGenerator(name="system_uuid", strategy = "uuid") // system_uuid 전략 사용, hibernate 제공 uuid 사용
    @Column(name = "id")
    private String id; // 아이디

    private String name; // 회원 이름

    @Column(unique = true, name="email") // 고유 데이터
    private String email; // 회원 이메일

    @Column(scale = 0) // 소수점 값 X
    private int point; // 포인트

    private String nickName; // 닉네임

    private String tel; // 전화번호

    private String password; // 비밀번호

    @Enumerated(EnumType.STRING) // Enum 타입, 문자열 저장
    private Role role; // 유저 권한

    private String authProvider; // 인증 제공자 (카카오톡, 깃허브)

    /*@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<QnaBoardEntity> qnaBoards = new HashSet<>();*/

    public static Member createMember(MemberFormDTO memberFormDto){//, PasswordEncoder passwordEncoder

        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setNickName(memberFormDto.getNickName());
        member.setTel(memberFormDto.getTel());
//        String password = passwordEncoder.encode(memberFormDto.getPassword()); // 비밀번호 암호화
//        member.setPassword(password);
        member.setPassword(memberFormDto.getPassword());
        member.setRole(Role.USER); // 회원가입은 기본 유저
        return member;
    }
}
