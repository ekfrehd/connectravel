package com.connectravel.service.impl;

import com.connectravel.domain.dto.PageRequestDTO;
import com.connectravel.domain.dto.PageResultDTO;
import com.connectravel.domain.dto.QnaBoardDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.QnaBoard;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.QnaBoardRepository;
import com.connectravel.service.QnaBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class QnaBoardServiceImpl implements QnaBoardService {

    private final QnaBoardRepository qnaBoardRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long createQna(QnaBoardDTO dto) {

        log.info("테스트 : " + dto);

        QnaBoard qnaBoard = dtoToEntity(dto, memberRepository);
        qnaBoardRepository.save(qnaBoard);

        return qnaBoard.getQbno();
    } // 게시글 등록

    @Override
    public QnaBoardDTO getQnaByQbno(Long qbno) {

        Object result = qnaBoardRepository.getBoardByQbno(qbno);
        Object[] arr = (Object[]) result;

        return entityToDTO((QnaBoard) arr[0], (Member) arr[1]);
    } // 게시글 조회

    @Override
    @Transactional
    public void updateQna(QnaBoardDTO qnaBoardDTO) {

        QnaBoard qnaBoard = qnaBoardRepository.getOne(qnaBoardDTO.getQbno());

        qnaBoard.changeTitle(qnaBoardDTO.getTitle());
        qnaBoard.changeContent(qnaBoardDTO.getContent());

        qnaBoardRepository.save(qnaBoard);
    } // 게시글 수정

    @Override
    @Transactional
    public void deleteQnaWithReplies(Long qbno) {
        qnaBoardRepository.deleteById(qbno);
    } // 게시글 삭제

    @Override
    public PageResultDTO<QnaBoardDTO, Object[]> getPaginatedQnas(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[], QnaBoardDTO> fn = (en -> entityToDTO((QnaBoard) en[0], (Member) en[1]));

        Page<Object[]> result;
        String[] type = pageRequestDTO.getType();
        Sort sort = Sort.by(Sort.Direction.DESC, "qbno");

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        result = qnaBoardRepository.searchPage(type, pageRequestDTO.getKeyword(), pageable);

        log.info("실행결과 : " + result);

        return new PageResultDTO<>(result, fn);
    }
    private QnaBoard dtoToEntity(QnaBoardDTO dto, MemberRepository memberRepository) {

        Member member = memberRepository.findByEmail(dto.getWriterEmail())
                .orElseThrow(() -> new EntityNotFoundException("Member with email " + dto.getWriterEmail() + " not found"));

        QnaBoard qnaBoard = QnaBoard.builder().qbno(dto.getQbno()).title(dto.getTitle()).content(dto.getContent()).member(member).build();

        return qnaBoard;
    }

    private QnaBoardDTO entityToDTO(QnaBoard qnaBoard, Member member) {

        QnaBoardDTO qnaBoardDTO = QnaBoardDTO.builder()
                .qbno(qnaBoard.getQbno())
                .title(qnaBoard.getTitle())
                .content(qnaBoard.getContent())
                .regDate(qnaBoard.getRegTime())
                .modDate(qnaBoard.getModTime())
                .writerEmail(member.getEmail())
                .writerName(member.getNickName())
                .build();

        return qnaBoardDTO;
    }

}