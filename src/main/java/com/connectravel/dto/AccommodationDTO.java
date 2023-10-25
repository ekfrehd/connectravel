package com.connectravel.dto;

import lombok.*;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationDTO {

    private Long ano;

    private String name; // 숙소이름

    private int postal; // 우편번호

    private String address; // 주소

    private String adminname; // 숙소 운영자 이름

    private String intro;

    @Column(scale = 0)
    private int count; // 숙소 예약 횟수

    private String category; // 숙소 종류

    private String region; // 지역

    private String tel; // 전화번호

    private String email;

    private String content; // 숙박업소 소개

    private Integer minPrice; // A숙소의 방의 최저가 나타내기

    private RoomDTO roomDTO;

    private List<ImgDTO> imgDTOList;// imgDTO list

    /*@Column(scale = 4)
    private double grade; // 평점
    private int reviewcount; // 리뷰수*/
}
