package com.connectravel.domain.dto;

import com.connectravel.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserManageResponse {

    private Long id;
    private String userName;
    private String nickName;
    private String email;
    private Integer mannerScore;



    public static UserManageResponse fromEntity(Member user){
        return UserManageResponse.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .nickName(user.getNickName())
                .email(user.getEmail())
//                .mannerScore(user.getManner())
                .build();
    }
}
