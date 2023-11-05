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
public class TourBoardReviewReplyDTO {

    private Long tbrrno;
    private String text;
    private String replyer;
    private Long tbrno;
    private LocalDateTime regDate, modDate;

}
