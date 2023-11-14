package com.connectravel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourBoardDTO {

    private Long tbno;

    private String category;

    private int postal;

    private String region;

    private String address;

    private String title;

    private String content;

    private int reviewCount;

    private double grade;

    private LocalDateTime regDate, modDate;

    private List<String> imgFiles;

}