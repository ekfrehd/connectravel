package com.connectravel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReplyDTO {

    private Long rrno;

    private Long rbno;

    private String content;

    private String replyer;

    private LocalDateTime regDate, modDate;

}