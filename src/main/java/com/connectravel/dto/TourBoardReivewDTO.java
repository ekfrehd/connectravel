package com.connectravel.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourBoardReivewDTO {

    private Long tbrno; // 리뷰 게시물 번호
    private Long tbno; // 투어 게시물 번호
    private double grade;
    private String content;
    private int recommend; // 추천
    private String writerEmail; //작성자 이메일
    private String writerName;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private boolean memberState;
}
