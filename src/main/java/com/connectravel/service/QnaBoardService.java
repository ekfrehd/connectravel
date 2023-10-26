package com.connectravel.service;

import com.connectravel.entity.QnaBoardEntity;
import com.connectravel.dto.QnaBoardDTO;
//import com.connectravel.dto.PageRequestDTO;
//import com.connectravel.dto.PageResultDTO;
//import com.connectravel.dto.QnaReplyDTO;
import com.connectravel.entity.QnaBoardEntity;
import com.connectravel.entity.Member;
import com.connectravel.repository.MemberRepository;
import org.springframework.data.domain.PageRequest;

public interface QnaBoardService {
    
    Long register(QnaBoardDTO dto); //등록
    QnaBoardDTO get(Long bno); //조회
    void modify(QnaBoardDTO qnaBoardDTO); //수정
    /*void removeWithReplies(Long bno); //삭제
    PageResultDTO<QnaBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO); // 리스트 출력*/


    // impl전에 interface가 먼저 실행 되는거니까 (OVERRIDE)
    // 보통 dto<->ENTITY에 관한 내용을 많이 써둠
    // 그러나, 개발자의 의도에 따라 인터페이스를 않쓰고 Entity나 Dto에 때려박아도 됨(저 처럼 Member에)

    default QnaBoardEntity dtoToEntity(QnaBoardDTO dto, MemberRepository memberRepository){

        Member member = memberRepository.findByEmail(dto.getWriterEmail());
        // Member객체 생성 - 리포지토리 실행결과를 담는다.
        // Member member = new Member(); X
        // 새 객체 생성은 당연히 새로운 행을 만든다는 소리니까! 참조가 안됨!
        
        QnaBoardEntity qnaBoard = QnaBoardEntity.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(member)
                .build();
        //상단에서 생성한 Member객체 활용 Board객체 생성
        
        return qnaBoard;
    }

    //default QnaBoardDTO entityToDTO(QnaBoardEntity qnaBoard, Member member, Long replyCount){
    default QnaBoardDTO entityToDTO(QnaBoardEntity qnaBoard, Member member){

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
