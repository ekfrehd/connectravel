package org.ezone.room.entity;

import lombok.*;
import org.ezone.room.constant.Role;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    // private String authProvider; // 인증 제공자 (카카오톡, 깃허브)



}
