package com.connectravel.service;

import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.QnaBoardDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoard;
import com.connectravel.repository.MemberRepository;

public interface QnaBoardService {

    Long register(QnaBoardDTO dto); //등록

    QnaBoardDTO get(Long bno); //조회

    void modify(QnaBoardDTO qnaBoardDTO); //수정

    void removeWithReplies(Long bno); //삭제

    PageResultDTO<QnaBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO); // 리스트 출력


}