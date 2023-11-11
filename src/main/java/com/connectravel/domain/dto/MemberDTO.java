package com.connectravel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

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

    @Column(scale = 0)
    private int point;

    private boolean del;

    private boolean social;

    private List<String> memberRoles;

}