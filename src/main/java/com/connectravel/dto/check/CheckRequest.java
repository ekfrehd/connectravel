package com.connectravel.dto.check;


import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckRequest {
    private String userName;
    private Long roomId;
}
