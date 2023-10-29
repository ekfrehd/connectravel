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

    default QnaBoard dtoToEntity(QnaBoardDTO dto, MemberRepository memberRepository){
        Member member = memberRepository.findByEmail(dto.getWriterEmail());
        // Member객체 생성 - 리포지토리 실행결과를 담는다.
        // Member member = new Member(); X, 새 객체 생성은 당연히 새로운 행을 만든다는 소리니까! 참조가 안됨!

        QnaBoard qnaBoard = QnaBoard.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(member)
                .build();
        //상단에서 생성한 Member객체 활용 Board객체 생성
        return qnaBoard;
    }

    //default QnaBoardDTO entityToDTO(QnaBoard qnaBoard, Member member, Long replyCount){
    default QnaBoardDTO entityToDTO(QnaBoard qnaBoard, Member member){

        QnaBoardDTO qnaBoardDTO = QnaBoardDTO.builder()
                .bno(qnaBoard.getBno())
                .title(qnaBoard.getTitle())
                .content(qnaBoard.getContent())
                .regDate(qnaBoard.getRegTime())
                .modDate(qnaBoard.getModTime())
                .createdBy(qnaBoard.getCreatedBy())
                .modifiedBy(qnaBoard.getModifiedBy())
                .writerEmail(member.getEmail())
                .writerName(member.getNickName())
                .build();

        return qnaBoardDTO;
    }
}
