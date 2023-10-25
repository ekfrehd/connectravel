package org.ezone.room.service;

import lombok.RequiredArgsConstructor;
import org.ezone.room.dto.QnaReplyDTO;
import org.ezone.room.entity.QnaBoard;
import org.ezone.room.entity.QnaReply;
import org.ezone.room.repository.MemberRepository;
import org.ezone.room.repository.QnaReplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return result.stream()
                .flatMap(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void modify(QnaReplyDTO qnaReplyDTO) {

        QnaReply qnaReply = dtoToEntity(qnaReplyDTO, memberRepository);

        qnaReplyRepository.save(qnaReply);
    }

    @Override
    public void remove(Long rno) {
        qnaReplyRepository.deleteById(rno);
    }
}
