package com.connectravel.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminBoardDTO {

    private Long bno;
    private String title;
    private String content;
    private String writerEmail; //작성자 이메일
    private String writerName;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int replyCount; //해당 게시글 댓글 수
    private String category; //카테고리


}
