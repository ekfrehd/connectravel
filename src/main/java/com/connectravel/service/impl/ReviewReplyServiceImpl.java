package com.connectravel.service.impl;

import com.connectravel.domain.dto.ReviewReplyDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.ReviewBoard;
import com.connectravel.domain.entity.ReviewReply;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ReviewBoardRepository;
import com.connectravel.repository.ReviewReplyRepository;
import com.connectravel.service.ReviewReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewReplyServiceImpl implements ReviewReplyService {

    private final ReviewReplyRepository reviewReplyRepository;

    private final MemberRepository memberRepository;

    private final ReviewBoardRepository reviewBoardRepository;

    @Override
    public Long createReviewReply(ReviewReplyDTO reviewReplyDTO) {

        ReviewReply reviewReply = dtoToEntity(reviewReplyDTO);
        reviewReplyRepository.save(reviewReply);

        return reviewReply.getRrno();
    }

    @Override
    public void updateReviewReply(ReviewReplyDTO reviewReplyDTO) {
        ReviewReply reviewReply = dtoToEntity(reviewReplyDTO);
        reviewReplyRepository.save(reviewReply);
    }

    public List<ReviewReplyDTO> getRepliesByReviewRbno(Long rbno) {

        List<ReviewReply> result = reviewReplyRepository.getRepliesByReviewBoardOrderByRrno(ReviewBoard.builder().rbno(rbno).build());

        log.info("ReviewReplyServiceImpl - getList() 결과: " + result);

        return result.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReviewReply(Long rrno) {
        reviewReplyRepository.deleteById(rrno);
    }

    /* 변환 메서드 */
    private ReviewReply dtoToEntity(ReviewReplyDTO reviewReplyDTO) {
        ReviewBoard reviewBoard = ReviewBoard.builder().rbno(reviewReplyDTO.getRbno()).build();

        Member member = memberRepository.findByEmail(reviewReplyDTO.getReplyer());
        if (member == null) {
            throw new RuntimeException("해당 이메일을 가진 회원을 찾을 수 없습니다: " + reviewReplyDTO.getReplyer());
        }

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