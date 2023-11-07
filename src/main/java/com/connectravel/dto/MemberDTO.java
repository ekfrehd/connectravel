package com.connectravel.dto;

import lombok.*;
import org.ezone.room.constant.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberDTO {

    private String id;
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;
    @NotEmpty(message = "닉네임은 필수 입력 값입니다.")
    private String nickName;
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    private String email;

    private String tel;

    private int point;
    private Role role;

}
