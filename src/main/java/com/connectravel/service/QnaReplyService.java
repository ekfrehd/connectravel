package com.connectravel.service;

import com.connectravel.dto.QnaReplyDTO;

import java.util.List;

public interface QnaReplyService {

    Long register(QnaReplyDTO qnaReplyDTO); //등록

    List<QnaReplyDTO> getList(Long bno); //특정 게시물의 댓글 목록(조회)

    void modify(QnaReplyDTO qnaReplyDTO); //수정

    void remove(Long rno); //삭제


}