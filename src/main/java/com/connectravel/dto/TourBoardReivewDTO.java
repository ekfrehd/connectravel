package com.connectravel.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourBoardReivewDTO {

    private Long tbrno; // 가이드 리뷰 일련번호
    private Long tbno; // 가이드 일련번호
    private double grade; // 평점
    private String content; // 리뷰 내용
    private int recommend; // 추천
    private String writerEmail; // 작성자 이메일
    private String writerName; // 작성자 이름
    private LocalDateTime regDate, modDate; // 등록일, 수정일
    private boolean memberState; //
}
