package com.connectravel.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {

    // DTO 리스트
    private List<DTO> dtoList;
    // 총 페이지 번호
    private int totalPage;
    // 현재 페이지 번호
    private int page;
    // 목록 사이즈
    private int size;
    // 시작 페이지 번호, 끝 페이지 번호
    private int start, end;
    // 이전, 다음
    private boolean prev, next;
    // 페이지 번호 목록
    private List<Integer> pageList;

    // result를 stream으로 변환 후 fn을 이용하여 DTO로 변환하여 dtoList에 저장
    // result의 총 페이지 수를 totalPage에 저장
    // 페이지 정보를 makePageList()를 이용하여 페이지 목록 생성
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {

        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    //
    private void makePageList(Pageable pageable) {

        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();
        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10; // 현재 페이지 번호기준 끝페이지 계산
        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
