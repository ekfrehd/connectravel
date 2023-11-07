package com.connectravel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationDTO {

    private Long ano;

    private String accommodationName;
    private String accommodationType;

    private String sellerName;
    private String sellerEmail;

    private int postal;
    private String region;
    private String address;
    private String tel;

    private String content; // 기본 정보, 편의시설 및 서비스, 판매자 정보
    private String intro; // "사장님 한마디" 같은 짧은 숙소 소개글

    private int count;
    private int reviewCount; // 리뷰수
    private double grade; // 숙소 평점

    private Integer minPrice; // 최저가 노출

    private List<RoomDTO> roomDTO;
    private List<String> imgFiles;
    private List<OptionDTO> optionDTO;

}