package com.connectravel.service;

import com.connectravel.dto.QnaReplyDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoard;
import com.connectravel.entity.QnaReply;
import com.connectravel.repository.MemberRepository;

import java.util.List;
import java.util.stream.Stream;

public interface QnaReplyService {

    Long register (QnaReplyDTO qnaReplyDTO); //등록

    List<QnaReplyDTO> getList (Long bno); //특정 게시물의 댓글 목록(조회)

    void modify (QnaReplyDTO qnaReplyDTO); //수정

    void remove (Long rno); //삭제

    default QnaReply dtoToEntity (QnaReplyDTO qnaReplyDTO, MemberRepository memberRepository) {

        // QnaReply 객체와 QnaBoard 객체 사이의 외래키 관계를 설정하기 위해
        // QnaBoard 객체를 먼저 생성
        // 주의 : new는 새 객체 생성이니까 하면 안됨!
        // 여긴 dto의 bno를 정의하고 객체 생성하니까 새 값이 생성 안되는거임.
        QnaBoard qnaBoard = QnaBoard.builder ().bno (qnaReplyDTO.getBno ()).build ();

        // QnaReply 객체와 Member 객체 사이의 외래키 관계를 설정하기 위해
        // MemberRepository를 사용하여 Member 객체를 조회
        Member member = memberRepository.findByEmail (qnaReplyDTO.getReplyer ());

        QnaReply qnaReply = QnaReply.builder ().rno (qnaReplyDTO.getRno ()).text (qnaReplyDTO.getText ()).member (member) // QnaReply 객체와 Member 객체 사이의 외래키 값을 설정
                .qnaBoard (qnaBoard) // QnaReply 객체와 QnaBoard 객체 사이의 외래키 값을 설정
                .build ();

        return qnaReply; // 생성된 QnaReply 객체를 반환
    }

    default Stream<QnaReplyDTO> entityToDTO (QnaReply qnaReply) {

        // QnaReply 객체의 id 필드를 조회하여 해당하는 Member 객체의 이메일 값을 추출함
        // Entity 필드명이 Member id 였으니까
        // 1. qnaReply.getMember하면 게시물의 작성자의 entity가 있냐고 조회
        // 2. qnaReply.getMember.getEmail (QnaReply.getMember.getEmail : qnaReply의 id필드의 email)
        // 즉, 외래키한 qnaReply에 등록된 id = Member의 id 이잖음?
        // 그 Member의 id행에 있는 email을 추출하는거
        String replyer = qnaReply.getMember () != null ? qnaReply.getMember ().getEmail () : null;

        /*QnaReplyDTO dto = QnaReplyDTO.builder()
                .rno(qnaReply.getRno())
                .text(qnaReply.getText())
                .replyer(replyer)
                .regDate(qnaReply.getRegTime())
                .modDate(qnaReply.getModTime())
                .build();*/

        QnaReplyDTO dto = QnaReplyDTO.builder ().rno (qnaReply.getRno ()).text (qnaReply.getText ()).regDate (qnaReply.getRegTime ()).modDate (qnaReply.getModTime ()).build ();

        // 생성된 QnaReplyDTO 객체를 Stream으로 감싸서 반환
        // 결과상 QnyReplyDTO가 여러개 일 수 있으니까, 한줄 형태인 stream 사용
        return Stream.of (dto);
    }
}
