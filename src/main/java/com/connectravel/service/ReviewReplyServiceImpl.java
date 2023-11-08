package com.connectravel.service;

import com.connectravel.dto.ReviewReplyDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.ReviewBoard;
import com.connectravel.entity.ReviewReply;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ReviewBoardRepository;
import com.connectravel.repository.ReviewReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewReplyServiceImpl implements ReviewReplyService {

    private final ReviewReplyRepository reviewReplyRepository;

    private final MemberRepository memberRepository;

    private final ReviewBoardRepository reviewBoardRepository;

    @Override
    public Long register(ReviewReplyDTO reviewReplyDTO) {
        ReviewReply reviewReply = dtoToEntity(reviewReplyDTO);
        reviewReplyRepository.save(reviewReply);
        return reviewReply.getRrno();
    }

    @Override
    public void modify(ReviewReplyDTO reviewReplyDTO) {
        ReviewReply reviewReply = dtoToEntity(reviewReplyDTO);
        reviewReplyRepository.save(reviewReply);
    }

    public List<ReviewReplyDTO> getList(Long rbno) {
        List<ReviewReply> result = reviewReplyRepository.getRepliesByReviewBoardOrderByRrno(ReviewBoard.builder().rbno(rbno).build());
        log.info("ReviewReplyServiceImpl - getList() 결과: " + result);
        return result.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void remove(Long rno) {
        reviewReplyRepository.deleteById(rno);
    }


    private ReviewReply dtoToEntity(ReviewReplyDTO reviewReplyDTO) {
        ReviewBoard reviewBoard = ReviewBoard.builder().rbno(reviewReplyDTO.getRbno()).build();
        Member member = memberRepository.findByEmail(reviewReplyDTO.getReplyer())
                .orElseThrow(() -> new RuntimeException("해당 이메일을 가진 회원을 찾을 수 없습니다: " + reviewReplyDTO.getReplyer()));

        return ReviewReply.builder()
                .rrno(reviewReplyDTO.getRrno())
                .content(reviewReplyDTO.getContent())
                .member(member)
                .reviewBoard(reviewBoard)
                .build();
    }

    private ReviewReplyDTO entityToDTO(ReviewReply reviewReply) {
        return ReviewReplyDTO.builder()
                .rrno(reviewReply.getRrno())
                .rbno(reviewReply.getReviewBoard().getRbno())
                .content(reviewReply.getContent())
                .replyer(reviewReply.getMember().getEmail())
                .regDate(reviewReply.getRegTime())
                .modDate(reviewReply.getModTime())
                .build();
    }
}
