package com.connectravel.domain.dto.Review;

import com.connectravel.constant.SportEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewResponse {

    private Long crewId;
    private Long joinUserId;
    private String joinUserNickName;
    private String userName;
    private Double userMannerScore;
    private List<SportEnum> sports;

}
