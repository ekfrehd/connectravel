package com.connectravel.entity;

import lombok.*;
import org.ezone.room.constant.Role;
import org.hibernate.annotations.GenericGenerator;

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
    private String password;

    @Column(unique = true)
    private String email;
    private String name;
    private String nickName;
    private String tel;

    @Column(scale = 0)
    private int point;

    @Enumerated(EnumType.STRING)
    private Role role;

}
