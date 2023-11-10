package com.connectravel.service;

import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.QnaBoardDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.QnaBoard;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.QnaBoardRepository;
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

    @Override // 게시글 등록
    @Transactional
    public Long register(QnaBoardDTO dto) {

        log.info("테스트 : " + dto);
        QnaBoard qnaBoard = dtoToEntity(dto, memberRepository);
        qnaBoardRepository.save(qnaBoard);
        return qnaBoard.getBno();
    }

    @Override // 게시글 조회
    public QnaBoardDTO get(Long bno) {

        Object result = qnaBoardRepository.getBoardByBno(bno); // 게시글 번호를 기반으로 데이터를 가져와 result에 저장
        Object[] arr = (Object[]) result; // result를 배열 형태로 arr에 저장
        return entityToDTO((QnaBoard) arr[0], (Member) arr[1]);
    }

    @Override // 게시글 수정
    @Transactional
    public void modify(QnaBoardDTO qnaBoardDTO) {
        QnaBoard qnaBoard = qnaBoardRepository.getOne(qnaBoardDTO.getBno());

        qnaBoard.changeTitle(qnaBoardDTO.getTitle());
        qnaBoard.changeContent(qnaBoardDTO.getContent());

        qnaBoardRepository.save(qnaBoard);
    }

    @Override // 게시글 삭제
    @Transactional
    public void removeWithReplies(Long bno) {
        qnaBoardRepository.deleteById(bno);
    }

    @Override
    public PageResultDTO<QnaBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[], QnaBoardDTO> fn = (en -> entityToDTO((QnaBoard) en[0], (Member) en[1]));

        Page<Object[]> result;
        String[] type = pageRequestDTO.getType();
        Sort sort = Sort.by(Sort.Direction.DESC, "bno");

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        result = qnaBoardRepository.searchPage(type, pageRequestDTO.getKeyword(), pageable);
        log.info("실행결과 : " + result);

        return new PageResultDTO<>(result, fn);
    }
    private QnaBoard dtoToEntity(QnaBoardDTO dto, MemberRepository memberRepository) {
        Member member = memberRepository.findByEmail(dto.getWriterEmail())
                .orElseThrow(() -> new EntityNotFoundException("Member with email " + dto.getWriterEmail() + " not found"));

        QnaBoard qnaBoard = QnaBoard.builder().bno(dto.getBno()).title(dto.getTitle()).content(dto.getContent()).member(member).build();
        return qnaBoard;
    }

    private QnaBoardDTO entityToDTO(QnaBoard qnaBoard, Member member) {
        QnaBoardDTO qnaBoardDTO = QnaBoardDTO.builder().bno(qnaBoard.getBno()).title(qnaBoard.getTitle()).content(qnaBoard.getContent()).regDate(qnaBoard.getRegTime()).modDate(qnaBoard.getModTime()).createdBy(qnaBoard.getCreatedBy()).modifiedBy(qnaBoard.getModifiedBy()).writerEmail(member.getEmail()).writerName(member.getNickName()).build();
        return qnaBoardDTO;
    }
}
