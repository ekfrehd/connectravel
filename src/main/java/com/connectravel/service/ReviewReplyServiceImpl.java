package com.connectravel.service;

import com.connectravel.dto.ReviewReplyDTO;
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

        ReviewReply reviewReply = dtoToEntity(reviewReplyDTO, memberRepository);

        reviewReplyRepository.save(reviewReply);

        return reviewReply.getRrno();
    }

    public List<ReviewReplyDTO> getList(Long rbno) {
        List<ReviewReply> result = reviewReplyRepository.getRepliesByReviewBoardOrderByRrno(ReviewBoard.builder().rbno(rbno).build());
        log.info("ReviewReplyServiceImpl - getList() 결과: " + result);
        return result.stream()
                .flatMap(this::entityToDTO)
                .collect(Collectors.toList());
    }



    @Override
    public void modify(ReviewReplyDTO reviewReplyDTO) {

        ReviewReply reviewReply = dtoToEntity(reviewReplyDTO, memberRepository);

        reviewReplyRepository.save(reviewReply);
    }

    @Override
    public void remove(Long rno) {
        reviewReplyRepository.deleteById(rno);
    }
}
