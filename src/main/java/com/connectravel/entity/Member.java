package com.connectravel.entity;

import lombok.*;
import org.ezone.room.constant.Role;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {

    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name="system_uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(scale = 0)
    private int point;

    private String nickName;

    private String tel;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;



}
