package com.connectravel.service;

import com.connectravel.dto.TourBoardReviewReplyDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.TourBoardReview;
import com.connectravel.entity.TourBoardReviewReply;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.TourBoardReviewReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class TourBoardReviewReplyServiceImpl implements TourBoardReviewReplyService {

    private final TourBoardReviewReplyRepository tourBoardReviewReplyRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long createTourBoardReviewReply(TourBoardReviewReplyDTO TourBoardReviewReplyDTO) {

        TourBoardReviewReply tourBoardReviewReply = dtoToEntity(TourBoardReviewReplyDTO, memberRepository);

        tourBoardReviewReplyRepository.save(tourBoardReviewReply);

        return tourBoardReviewReply.getTbrrno();
    }

    @Override
    public TourBoardReviewReplyDTO getTourBoardReviewReply(Long tbrrno) {
        TourBoardReviewReply tourBoardReviewReply = tourBoardReviewReplyRepository.findById(tbrrno)
                .orElseThrow(() -> new EntityNotFoundException("TourBoardReviewReply not found"));

        return entityToDTO(tourBoardReviewReply);
    }

    @Override
    public void updateTourBoardReviewReply(TourBoardReviewReplyDTO TourBoardReviewReplyDTO) {

        Optional<TourBoardReviewReply> optionalTourBoardReviewReply = tourBoardReviewReplyRepository.findById(TourBoardReviewReplyDTO.getTbrrno());
        TourBoardReviewReply tourBoardReviewReply = optionalTourBoardReviewReply.orElseThrow(() -> new NoSuchElementException("댓글이 존재하지 않습니다."));

        tourBoardReviewReply.changeContent(TourBoardReviewReplyDTO.getContent());
        tourBoardReviewReplyRepository.save(tourBoardReviewReply);
    }

    @Override
    public void deleteTourBoardReviewReply(Long tbrrno) {
        tourBoardReviewReplyRepository.deleteById(tbrrno);
    }

    private TourBoardReviewReply dtoToEntity(TourBoardReviewReplyDTO TourBoardReviewReplyDTO, MemberRepository memberRepository){

        TourBoardReview tourBoardReview = TourBoardReview.builder().tbrno(TourBoardReviewReplyDTO.getTbrno()).build();

        Member member = memberRepository.findByEmail(TourBoardReviewReplyDTO.getReplyer())
                .orElseThrow(() -> new EntityNotFoundException("Member with email " + TourBoardReviewReplyDTO.getReplyer() + " not found"));

        return TourBoardReviewReply.builder()
                .tbrrno(TourBoardReviewReplyDTO.getTbrrno())
                .content(TourBoardReviewReplyDTO.getContent())
                .member(member)
                .tourBoardReview(tourBoardReview)
                .build();

    }

    private TourBoardReviewReplyDTO entityToDTO(TourBoardReviewReply tourBoardReviewReply) {

        return TourBoardReviewReplyDTO.builder()
                .tbrrno(tourBoardReviewReply.getTbrrno())
                .tbrno(tourBoardReviewReply.getTourBoardReview().getTbrno())
                .content(tourBoardReviewReply.getContent())
                .replyer(tourBoardReviewReply.getMember().getEmail())
                .regDate(tourBoardReviewReply.getRegTime())
                .modDate(tourBoardReviewReply.getModTime())
                .build();

    }
}
