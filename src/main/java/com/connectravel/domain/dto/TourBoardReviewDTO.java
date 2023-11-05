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
public class TourBoardReviewDTO {

    private Long tbrno;

    private Long tbno;

    private String content;

    private double grade;

    private String writerEmail;

    private String writerName;

    private LocalDateTime regDate, modDate;

    private int recommend; // 추천 수

}