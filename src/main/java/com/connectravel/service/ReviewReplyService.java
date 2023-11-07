package com.connectravel.service;

import com.connectravel.dto.ReviewReplyDTO;

import java.util.List;

public interface ReviewReplyService {

    Long register(ReviewReplyDTO reviewReplyDTO); //등록
    List<ReviewReplyDTO> getList(Long bno); //특정 게시물의 댓글 목록(조회)
    void modify(ReviewReplyDTO reviewReplyDTO); //수정
    void remove(Long rno); //삭제

}
