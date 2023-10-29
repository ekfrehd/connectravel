package com.connectravel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TourBoardDTO {

    private Long tbno;
    private String title;
    private String content;
    private String region; // 지역
    private String category; // 카테고리
    private double grade; // 평점
    private int reviewCount; // 리뷰수
    private int postal; // 우편번호
    private String address; // 주소
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private List<ImgDTO> imgDTOList;// imgDTO list
    public void setImgDTOList(List<ImgDTO> imgDTOList) {
        this.imgDTOList = imgDTOList;
    } // 이미지 리스트화
}
