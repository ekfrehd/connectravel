package com.connectravel.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationDTO {

    private Long ano;

    private String accommodationName;

    private int postal;

    private String address;

    private String sellerName;

    private int count;

    private String accommodationType;

    private String region;

    private String tel;

    private String email;

    private String intro; // "사장님 한마디" 같은 짧은 숙소 소개글

    private String content; // 기본 정보, 편의시설 및 서비스, 판매자 정보

    private Integer minPrice; // A 숙소의 방의 최저가 나타내기

    private List<RoomDTO> roomDTO;
    private List<String> imgFiles;
    private List<OptionDTO> optionDTO;

}