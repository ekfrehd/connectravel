package com.connectravel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

    private Long id;

    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private String nickName;

    private String tel;
    private String tel1, tel2, tel3; // 전화번호 sprit

    @Column(scale = 0)
    private int point;

    private boolean del;

    private boolean social;

    private Set<String> memberRoles;

}