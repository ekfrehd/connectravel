package com.connectravel.service;

import com.connectravel.dto.QnaReplyDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoard;
import com.connectravel.entity.QnaReply;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.QnaReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class QnaReplyServiceImpl implements QnaReplyService {

    private final QnaReplyRepository qnaReplyRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long register(QnaReplyDTO qnaReplyDTO) {
        QnaReply qnaReply = dtoToEntity(qnaReplyDTO, memberRepository);

        qnaReplyRepository.save(qnaReply);

        return qnaReply.getRno();
    }

    public List<QnaReplyDTO> getList(Long bno) {

        List<QnaReply> result = qnaReplyRepository.getRepliesByQnaBoardOrderByRno(QnaBoard.builder().bno(bno).build());
        return result.stream().flatMap(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    public void modify(QnaReplyDTO qnaReplyDTO) {

        Optional<QnaReply> OptionalQnaReply = qnaReplyRepository.findById(qnaReplyDTO.getRno());
        QnaReply qnaReply = OptionalQnaReply.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다."));

        qnaReply.changeContent(qnaReplyDTO.getContent());
        qnaReplyRepository.save(qnaReply);
    }

    @Override
    public void remove(Long rno) {
        qnaReplyRepository.deleteById(rno);
    }

    private QnaReply dtoToEntity(QnaReplyDTO qnaReplyDTO, MemberRepository memberRepository) {

        QnaBoard qnaBoard = QnaBoard.builder().bno(qnaReplyDTO.getBno()).build();

        Member member = memberRepository.findByEmail(dto.getWriterEmail())
                .orElseThrow(() -> new EntityNotFoundException("Member with email " + dto.getWriterEmail() + " not found"));

        QnaReply qnaReply = QnaReply.builder().rno(qnaReplyDTO.getRno()).content(qnaReplyDTO.getContent()).member(member) // QnaReply 객체와 Member 객체 사이의 외래키 값을 설정
                .qnaBoard(qnaBoard) // QnaReply 객체와 QnaBoard 객체 사이의 외래키 값을 설정
                .build();

        return qnaReply; // 생성된 QnaReply 객체를 반환
    }

    private Stream<QnaReplyDTO> entityToDTO(QnaReply qnaReply) {

        String replyer = qnaReply.getMember() != null ? qnaReply.getMember().getEmail() : null;


        QnaReplyDTO dto = QnaReplyDTO.builder().rno(qnaReply.getRno()).content(qnaReply.getContent()).regDate(qnaReply.getRegTime()).modDate(qnaReply.getModTime()).build();

        return Stream.of(dto);
    }
}
