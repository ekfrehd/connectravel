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

    public List<QnaReplyDTO> getList(Long bno) { // 게시글 번호를 기반으로 댓글을 가져오는 메서드

        // 리뷰 댓글을 게시글의 게시글 번호로 정렬
        // 엔티티를 DTO로 변환
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
