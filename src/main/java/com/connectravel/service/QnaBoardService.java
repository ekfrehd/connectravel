package com.connectravel.service;

import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.QnaBoardDTO;

public interface QnaBoardService {

    Long createQna(QnaBoardDTO dto);

    QnaBoardDTO getQnaByQbno(Long qbno);

    void updateQna(QnaBoardDTO qnaBoardDTO);

    void deleteQnaWithReplies(Long qbno);

    PageResultDTO<QnaBoardDTO, Object[]> getPaginatedQnas(PageRequestDTO pageRequestDTO);


}