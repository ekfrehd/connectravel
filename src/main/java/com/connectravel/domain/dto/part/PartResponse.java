package com.connectravel.domain.dto.part;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PartResponse {
    private Integer now;
    private Integer limit;
    private Integer status;
    private String title;
    private String body;


}
