package com.connectravel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewReplyDTO {

    private Long rrno;
    private Long rbno;

    private String content;
    private String replyer;
    private LocalDateTime regDate, modDate;

}
