package com.connectravel.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString(exclude = {"memberRoles"})
@EqualsAndHashCode(of = "id")
public class Member extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private String nickName;

    private String tel;

    @Column(scale = 0)
    private int point;

    private boolean del;

    private boolean social;

    @ManyToMany(fetch = FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinTable(name = "member_roles", joinColumns = { @JoinColumn(name = "member_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> memberRoles = new HashSet<>();

}