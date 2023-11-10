package com.connectravel.service;

import com.connectravel.dto.QnaReplyDTO;

import java.util.List;

public interface QnaReplyService {

    Long createQnaReply(QnaReplyDTO qnaReplyDTO); //등록

    List<QnaReplyDTO> getQnaRepliesByQbno(Long qbno); //특정 게시물의 댓글 목록(조회)

    void updateQnaReply(QnaReplyDTO qnaReplyDTO); //수정

    void deleteQnaReply(Long qrno); //삭제


}