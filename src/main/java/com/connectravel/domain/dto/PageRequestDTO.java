package com.connectravel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@Builder
@AllArgsConstructor
public class PageRequestDTO {

    private int page;

    private int size;

    private String type; // 검색의 종류 t, c, tc, tw, twc

    private String keyword; // 검색어

    private String category; // 숙소 종류

    private String region; // 지역

    public String[] getType() {
        if (type == null || type.isEmpty()) {
            return null;
        }

        return type.split("");
    }

    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    public PageRequestDTO(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public Pageable getPageable(String...props) {
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }

}