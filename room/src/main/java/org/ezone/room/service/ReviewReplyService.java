package org.ezone.room.service;

import org.ezone.room.dto.QnaReplyDTO;
import org.ezone.room.dto.ReviewReplyDTO;
import org.ezone.room.entity.*;
import org.ezone.room.repository.MemberRepository;

import java.util.List;
import java.util.stream.Stream;

public interface ReviewReplyService {

    Long register(ReviewReplyDTO reviewReplyDTO); //등록
    List<ReviewReplyDTO> getList(Long bno); //특정 게시물의 댓글 목록(조회)
    void modify(ReviewReplyDTO reviewReplyDTO); //수정
    void remove(Long rno); //삭제

    default ReviewReply dtoToEntity(ReviewReplyDTO reviewReplyDTO, MemberRepository memberRepository){

        ReviewBoard reviewBoard = ReviewBoard.builder().rbno(reviewReplyDTO.getRbno()).build();

        Member member = memberRepository.findByEmail(reviewReplyDTO.getReplyer());

        ReviewReply reviewReply = ReviewReply.builder()
                .rrno(reviewReplyDTO.getRrno())
                .text(reviewReplyDTO.getText())
                .member(member)
                .reviewBoard(reviewBoard)
                .build();

        return reviewReply;
    }

    default Stream<ReviewReplyDTO> entityToDTO(ReviewReply reviewReply) {

        String replyer = reviewReply.getMember() != null ? reviewReply.getMember().getEmail() : null;

        ReviewReplyDTO dto = ReviewReplyDTO.builder()
                .rrno(reviewReply.getRrno())
                .text(reviewReply.getText())
                .replyer(replyer)
                .regDate(reviewReply.getRegTime())
                .modDate(reviewReply.getModTime())
                .build();


        return Stream.of(dto);
    }

}
