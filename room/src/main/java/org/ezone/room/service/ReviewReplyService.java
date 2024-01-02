package org.ezone.room.service;

import java.util.List;
import java.util.stream.Stream;
import org.ezone.room.dto.ReviewReplyDTO;
import org.ezone.room.entity.Member;
import org.ezone.room.entity.ReviewBoard;
import org.ezone.room.entity.ReviewReply;
import org.ezone.room.repository.MemberRepository;
import org.springframework.stereotype.Service;

public interface ReviewReplyService {

    Long register(ReviewReplyDTO reviewReplyDTO); //등록
    List<ReviewReplyDTO> getList(Long bno); //특정 게시물의 댓글 목록(조회)
    void modify(ReviewReplyDTO reviewReplyDTO); //수정
    void remove(Long rno); //삭제

    default ReviewReply dtoToEntity(ReviewReplyDTO reviewReplyDTO, MemberRepository memberRepository){

        ReviewBoard reviewBoard = ReviewBoard.builder().rbno(reviewReplyDTO.getRbno()).build();

        Member member = memberRepository.findByEmail(reviewReplyDTO.getReplyer());
//        member = Member.builder().email(reviewReplyDTO.getReplyer()).build();
        ReviewReply reviewReply = ReviewReply.builder()
                .rrno(reviewReplyDTO.getRrno())
                .text(reviewReplyDTO.getText())
                .member(member)
                .reviewBoard(reviewBoard)
                .build();

        return reviewReply;
    }

    default Stream<ReviewReplyDTO> entityToDTO(ReviewReply reviewReply) {

//        String replyer = reviewReply.getMember() != null ? reviewReply.getMember().getEmail() : null;

        ReviewReplyDTO dto = ReviewReplyDTO.builder()
                .rrno(reviewReply.getRrno())
                .text(reviewReply.getText())
                .replyer(reviewReply.getMember().getEmail())
                .regDate(reviewReply.getRegTime())
                .modDate(reviewReply.getModTime())
                .build();


        return Stream.of(dto);
    }

}
