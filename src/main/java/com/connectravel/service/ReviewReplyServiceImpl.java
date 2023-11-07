package org.ezone.room.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.dto.ReviewReplyDTO;
import org.ezone.room.entity.ReviewBoard;
import org.ezone.room.entity.ReviewReply;
import org.ezone.room.repository.MemberRepository;
import org.ezone.room.repository.ReviewBoardRepository;
import org.ezone.room.repository.ReviewReplyRepository;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewReplyServiceImpl implements ReviewReplyService {

    private final ReviewReplyRepository reviewReplyRepository;

    private final MemberRepository memberRepository;

    private final ReviewBoardRepository reviewBoardRepository;

    @Override
    public Long register(ReviewReplyDTO reviewReplyDTO) {

//        ReviewBoard reviewBoard = reviewBoardRepository.findById(reviewReplyDTO.getRbno())
//                .orElseThrow(() -> new IllegalArgumentException("No item found with ID: " + reviewReplyDTO.getRbno()));

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
