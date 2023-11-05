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
public class AdminBoardDTO {

    private Long abno;

    private String title;
    private String content;
    private String category;

    private int replyCount;
    private LocalDateTime regDate, modDate;

    private String writerEmail;
    private String writerName;

}