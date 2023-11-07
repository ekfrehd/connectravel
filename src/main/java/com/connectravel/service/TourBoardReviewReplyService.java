package com.connectravel.service;

import com.connectravel.dto.TourBoardReviewReplyDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.TourBoardReview;
import com.connectravel.entity.TourBoardReviewReply;
import com.connectravel.repository.MemberRepository;

import java.util.stream.Stream;

public interface TourBoardReviewReplyService {

    Long register(TourBoardReviewReplyDTO TourBoardReviewReplyDTO); //등록

   /*List<TourBoardReviewReplyDTO> getList(Long bno); //특정 게시물의 댓글 목록(조회)*/

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

//        String replyer = reviewReply.getMember() != null ? reviewReply.getMember().getEmail() : null;

        TourBoardReviewReplyDTO dto = TourBoardReviewReplyDTO.builder()
                .tbrrno(tourBoardReviewReply.getTbrrno())
                .tbrno(tourBoardReviewReply.getTourBoardReview().getTbrno())
                .text(tourBoardReviewReply.getText())
                .replyer(tourBoardReviewReply.getMember().getEmail())
                .regDate(tourBoardReviewReply.getRegTime())
                .modDate(tourBoardReviewReply.getModTime())
                .build();

        return Stream.of(dto);
    }

}
