package com.connectravel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TourBoardDTO {

    private Long tbno; // 가이드 일련번호
    @NotBlank(message = "제목을 입력해주세요.")
    private String title; // 가이드 제목
    @NotBlank(message = "내용을 입력해주세요.")
    private String content; //가이드 내용
    private String region; // 지역
    private String category; // 카테고리
    private double grade; // 평점
    private int reviewCount; // 리뷰수
    @NotBlank(message = "우편번호를 입력해주세요.")
    private int postal; // 우편번호
    @NotBlank(message = "주소를 입력해주세요.")
    private String address; // 주소
    private LocalDateTime regDate, modDate; // 등록일, 수정일

    private List<ImgDTO> imgDTOList;// imgDTO list

    public void setImgDTOList(List<ImgDTO> imgDTOList) {
        this.imgDTOList = imgDTOList;
    } // 이미지 리스트화
}
