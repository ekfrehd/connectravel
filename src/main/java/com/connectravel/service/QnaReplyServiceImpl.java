package com.connectravel.service;

import com.connectravel.dto.QnaReplyDTO;
import com.connectravel.entity.QnaBoard;
import com.connectravel.entity.QnaReply;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.QnaReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

        Optional<QnaReply> qnaReplyOptional = qnaReplyRepository.findById(qnaReplyDTO.getRno());//rno와 같은 댓글을 DB에서 찾는다.
        QnaReply qnaReply = qnaReplyOptional.orElseThrow();//댓글이 존재하지 않는 경우 예외처리 전달
        qnaReply.changeText(qnaReplyDTO.getText());//댓글 내용을 주어진 replyDTO 내용으로 변경
        qnaReplyRepository.save(qnaReply);//변경한 댓글을 DB에 저장
        /*QnaReply qnaReply = dtoToEntity(qnaReplyDTO, memberRepository);
        qnaReplyRepository.save(qnaReply);*/
    }

    @Override
    public void remove(Long rno) {
        qnaReplyRepository.deleteById(rno);
    }
}
