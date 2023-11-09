package com.connectravel.service;

import com.connectravel.dto.TourBoardReviewReplyDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.TourBoardReview;
import com.connectravel.entity.TourBoardReviewReply;
import com.connectravel.repository.MemberRepository;

import java.util.List;

import java.util.stream.Stream;

public interface TourBoardReviewReplyService {

    Long register(TourBoardReviewReplyDTO TourBoardReviewReplyDTO); //등록

    List<TourBoardReviewReplyDTO> getList(Long tbrno); //특정 게시물의 댓글 목록(조회)


    void modify(TourBoardReviewReplyDTO TourBoardReviewReplyDTO); //수정

    void remove(Long rno); //삭제*/

    default TourBoardReviewReply dtoToEntity(TourBoardReviewReplyDTO TourBoardReviewReplyDTO, MemberRepository memberRepository){

        TourBoardReview tourBoardReview = TourBoardReview.builder().tbrno(TourBoardReviewReplyDTO.getTbrno()).build();

        /*Member member = memberRepository.findByEmail(TourBoardReviewReplyDTO.getReplyer());*/
//        member = Member.builder().email(TourBoardReviewReplyDTO.getReplyer()).build();
        Member member = memberRepository.findByEmail("sample@sample.com");

        TourBoardReviewReply reviewReply = TourBoardReviewReply.builder()
                .tbrrno(TourBoardReviewReplyDTO.getTbrno())
                .text(TourBoardReviewReplyDTO.getText())
                .member(member)
                .tourBoardReview(tourBoardReview)
                .build();

        return reviewReply;
    }

    default Stream<TourBoardReviewReplyDTO> entityToDTO(TourBoardReviewReply tourBoardReviewReply) {

        String replyer = tourBoardReviewReply.getMember() != null ? tourBoardReviewReply.getMember().getEmail() : null;

        /*QnaReplyDTO dto = QnaReplyDTO.builder()
                .rno(qnaReply.getRno())
                .text(qnaReply.getText())
                .replyer(replyer)
                .regDate(qnaReply.getRegTime())
                .modDate(qnaReply.getModTime())
                .build();*/

        TourBoardReviewReplyDTO dto = TourBoardReviewReplyDTO.builder().tbrrno(tourBoardReviewReply.getTbrrno()).text(tourBoardReviewReply.getText()).regDate(tourBoardReviewReply.getRegTime()).modDate(tourBoardReviewReply.getModTime()).build();

        // 생성된 QnaReplyDTO 객체를 Stream으로 감싸서 반환
        // 결과상 QnyReplyDTO가 여러개 일 수 있으니까, 한줄 형태인 stream 사용
        return Stream.of(dto);
    }
}
