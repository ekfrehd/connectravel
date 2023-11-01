package com.connectravel.service;

import com.connectravel.dto.QnaReplyDTO;
import com.connectravel.entity.QnaBoard;
import com.connectravel.entity.QnaReply;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.QnaReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        qnaReply.changeText(qnaReplyDTO.getText());
        qnaReplyRepository.save(qnaReply); //수정된 객체를 repository에 저장
    }

    @Override
    public void remove(Long rno) {
        qnaReplyRepository.deleteById(rno);
    }
}
