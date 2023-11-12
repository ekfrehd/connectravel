package com.connectravel.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationSearchDTO {
    private Long ano;
    private String accommodationName;
    private Double grade; // 평점
    private String region; // 지역

    private List<LocalDate> availableDates; // 사용 가능 날짜
    private String priceRange; // 가격 범위

    private String mainImage; // 대표 이미지 파일명
    private List<String> selectedOptions; // 선택된 옵션들

    private boolean operating;

    public boolean isOperating() {
        return this.operating;
    }
    // 생성자, getter, setter 등 기타 필요한 메서드 추가...
}

