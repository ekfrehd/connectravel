package com.connectravel.service;

import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.QnaBoardDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoard;
import com.connectravel.repository.MemberRepository;

public interface QnaBoardService {

    Long register (QnaBoardDTO dto); //등록

    QnaBoardDTO get (Long bno); //조회

    void modify (QnaBoardDTO qnaBoardDTO); //수정

    void removeWithReplies (Long bno); //삭제

    PageResultDTO<QnaBoardDTO, Object[]> getList (PageRequestDTO pageRequestDTO); // 리스트 출력

    default QnaBoard dtoToEntity (QnaBoardDTO dto, MemberRepository memberRepository) {
        Member member = memberRepository.findByEmail (dto.getWriterEmail ());
        QnaBoard qnaBoard = QnaBoard.builder ().bno (dto.getBno ()).title (dto.getTitle ()).content (dto.getContent ()).member (member).build ();
        return qnaBoard;
    }

    default QnaBoardDTO entityToDTO (QnaBoard qnaBoard, Member member) {
        /*QnaBoardDTO qnaBoardDTO = QnaBoardDTO.builder()
                .bno(qnaBoard.getBno())
                .title(qnaBoard.getTitle())
                .content(qnaBoard.getContent())
                .regDate(qnaBoard.getRegTime())
                .modDate(qnaBoard.getModTime())
                .createdBy(qnaBoard.getCreatedBy())
                .modifiedBy(qnaBoard.getModifiedBy())
                .writerEmail(member.getEmail())
                .writerName(member.getNickName())
                .build();*/

        QnaBoardDTO qnaBoardDTO = QnaBoardDTO.builder ().bno (qnaBoard.getBno ()).title (qnaBoard.getTitle ()).content (qnaBoard.getContent ())
                .regDate (qnaBoard.getRegTime ()).modDate (qnaBoard.getModTime ()).createdBy (qnaBoard.getCreatedBy ()).modifiedBy (qnaBoard.getModifiedBy ()).build ();

        return qnaBoardDTO;
    }
}
