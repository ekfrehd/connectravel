package com.connectravel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    private int page; // 1 페이지
    private int size; // 검색 개수
    private String type; // 검색의 종류 t, c, tc, tw, twc
    private String keyword; // 검색어
    private String category; // 숙소종류
    private String region; // 지역
    private String address; // 주소

    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    public PageRequestDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }

    // 검색 조건 배열 반환
    public String[] getType() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        return type.split("");
    }

    //페이징 처리
    public Pageable getPageable(String... props) {
        return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
    }
}
