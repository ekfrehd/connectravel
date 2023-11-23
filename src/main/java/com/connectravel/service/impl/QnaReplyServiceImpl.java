package com.connectravel.service.impl;

import com.connectravel.domain.dto.QnaReplyDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.QnaBoard;
import com.connectravel.domain.entity.QnaReply;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.QnaReplyRepository;
import com.connectravel.service.QnaReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaReplyServiceImpl implements QnaReplyService {

    private final QnaReplyRepository qnaReplyRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long createQnaReply(QnaReplyDTO qnaReplyDTO) {


        QnaReply qnaReply = dtoToEntity(qnaReplyDTO, memberRepository);

        qnaReplyRepository.save(qnaReply);

        return qnaReply.getQrno();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QnaReplyDTO> getQnaRepliesByQbno(Long qbno) {

        List<QnaReply> result = qnaReplyRepository.getRepliesByQnaBoardOrderByQrno(QnaBoard.builder().qbno(qbno).build());

        return result.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public void updateQnaReply(QnaReplyDTO qnaReplyDTO) {

        Optional<QnaReply> OptionalQnaReply = qnaReplyRepository.findById(qnaReplyDTO.getQrno());
        QnaReply qnaReply = OptionalQnaReply.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다."));

        qnaReply.changeContent(qnaReplyDTO.getContent());
        qnaReplyRepository.save(qnaReply);

    }

    @Override
    public void deleteQnaReply(Long qrno) {
        qnaReplyRepository.deleteById(qrno);
    }


    /* 변환 메서드 */
    private QnaReply dtoToEntity(QnaReplyDTO qnaReplyDTO, MemberRepository memberRepository) {

        QnaBoard qnaBoard = QnaBoard.builder().qbno(qnaReplyDTO.getQbno()).build();

        Optional<Member> memberOptional = memberRepository.findByEmail(qnaReplyDTO.getReplyer());

        Member member = memberOptional.orElseThrow(() ->
                new EntityNotFoundException("Member with email " + qnaReplyDTO.getReplyer() + " not found"));

        return QnaReply.builder()
                .qrno(qnaReplyDTO.getQrno())
                .content(qnaReplyDTO.getContent())
                .member(member)
                .qnaBoard(qnaBoard)
                .build();
    }

    private QnaReplyDTO entityToDTO(QnaReply qnaReply) {

        return QnaReplyDTO.builder()
                .qrno(qnaReply.getQrno())
                .qbno(qnaReply.getQnaBoard().getQbno())
                .content(qnaReply.getContent())
                .replyer(qnaReply.getMember().getEmail())
                .regDate(qnaReply.getRegTime())
                .modDate(qnaReply.getModTime())
                .build();
    }

}