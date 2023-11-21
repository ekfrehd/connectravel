package com.connectravel.service.impl;

import com.connectravel.domain.dto.TourBoardReviewReplyDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.TourBoardReview;
import com.connectravel.domain.entity.TourBoardReviewReply;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.TourBoardReviewReplyRepository;
import com.connectravel.service.TourBoardReviewReplyService;
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

    private TourBoardReviewReply dtoToEntity(TourBoardReviewReplyDTO tourBoardReviewReplyDTO, MemberRepository memberRepository) {

        TourBoardReview tourBoardReview = TourBoardReview.builder()
                .tbrno(tourBoardReviewReplyDTO.getTbrno())
                .build();

        Optional<Member> memberOptional = memberRepository.findByEmail(tourBoardReviewReplyDTO.getReplyer());

        Member member = memberOptional.orElseThrow(() ->
                new EntityNotFoundException("Member with email " + tourBoardReviewReplyDTO.getReplyer() + " not found"));

        return TourBoardReviewReply.builder()
                .tbrrno(tourBoardReviewReplyDTO.getTbrrno())
                .content(tourBoardReviewReplyDTO.getContent())
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