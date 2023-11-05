package com.connectravel.service;

import com.connectravel.dto.TourBoardReviewReplyDTO;
import com.connectravel.entity.TourBoardReview;
import com.connectravel.entity.TourBoardReviewReply;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.TourBoardReviewReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class TourBoardReviewReplyServiceImpl implements TourBoardReviewReplyService {

    private final TourBoardReviewReplyRepository tourBoardReviewReplyRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long register(TourBoardReviewReplyDTO TourBoardReviewReplyDTO) {

//        ReviewBoard reviewBoard = reviewBoardRepository.findById(TourBoardReviewReplyDTO.getRbno())
//                .orElseThrow(() -> new IllegalArgumentException("No item found with ID: " + TourBoardReviewReplyDTO.getRbno()));
        TourBoardReviewReply tourBoardReviewReply = dtoToEntity(TourBoardReviewReplyDTO, memberRepository);

        tourBoardReviewReplyRepository.save(tourBoardReviewReply);

        return tourBoardReviewReply.getTbrrno();
    }

    public List<TourBoardReviewReplyDTO> getList(Long tbrno) {
        List<TourBoardReviewReply> result = tourBoardReviewReplyRepository.getRepliesByTourBoardReviewOrderByTbrrno(TourBoardReview.builder().tbrno(tbrno).build());
        log.info("ReviewReplyServiceImpl - getList() 결과: " + result);
        return result.stream()
                .flatMap(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void modify(TourBoardReviewReplyDTO TourBoardReviewReplyDTO) {

        Optional<TourBoardReviewReply> optionalTourBoardReviewReply = tourBoardReviewReplyRepository.findById(TourBoardReviewReplyDTO.getTbrrno());
        TourBoardReviewReply tourBoardReviewReply = optionalTourBoardReviewReply.orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다."));

        tourBoardReviewReply.changeText(TourBoardReviewReplyDTO.getText());
        tourBoardReviewReplyRepository.save(tourBoardReviewReply); //수정된 객체를 repository에 저장
    }

    @Override
    public void remove(Long tbrno) {
        tourBoardReviewReplyRepository.deleteById(tbrno);
    }
}
