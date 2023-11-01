package com.connectravel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionDTO {

    private String optionName; // 예: "세탁기", "와이파이" 등
    private String optionCategory; // 예: "공용", "개별", "기타" 등
}

