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

import javax.transaction.Transactional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class QnaBoardServiceImpl implements QnaBoardService {

    //의존성 자동 주입
    private final QnaBoardRepository qnaBoardRepository;
    //private final QnaReplyRepository qnaReplyRepository;
    private final MemberRepository memberRepository; // Member 객체를 불러오기 위해 필요

    @Override // 게시글 등록
    @Transactional
    public Long register (QnaBoardDTO dto) {

        log.info ("테스트 : " + dto);

        // memberRepository를 넣으면 불러오기 끝!
        // 보통 dto로 불러올 수 있는 내용이면 dto를 넣고, dto로 불가하면 repository를 실행하면 됨!
        QnaBoard qnaBoard = dtoToEntity (dto, memberRepository);

        qnaBoardRepository.save (qnaBoard);

        return qnaBoard.getBno ();
    }

    @Override // 게시글 조회
    public QnaBoardDTO get (Long bno) {
        Object result = qnaBoardRepository.getBoardByBno (bno); // 게시글 번호를 기반으로 데이터를 가져와 result에 저장

        Object[] arr = (Object[]) result; // result를 배열 형태로 arr에 저장

        return entityToDTO ((QnaBoard) arr[0], (Member) arr[1]);
    }

    @Override // 게시글 수정
    @Transactional
    public void modify (QnaBoardDTO qnaBoardDTO) {
        QnaBoard qnaBoard = qnaBoardRepository.getOne (qnaBoardDTO.getBno ()); //boardRepository에서 Board객체를 받아
        //필요한 순간까지 로딩을 지연하는 getOne메서드 이용

        qnaBoard.changeTitle (qnaBoardDTO.getTitle ()); //Board객체의 제목을 수정
        qnaBoard.changeContent (qnaBoardDTO.getContent ()); //내용을 수정

        qnaBoardRepository.save (qnaBoard); //수정된 객체를 repository에 저장
    }

    @Override // 게시글 삭제
    @Transactional
    public void removeWithReplies (Long bno) {
        qnaBoardRepository.deleteById (bno); //이후 본 글 삭제
    }

    @Override
    public PageResultDTO<QnaBoardDTO, Object[]> getList (PageRequestDTO pageRequestDTO) {

        log.info (pageRequestDTO);

        Function<Object[], QnaBoardDTO> fn = (en -> entityToDTO ((QnaBoard) en[0], (Member) en[1]));

        // SearchBoardRepository에서 정의한 내용으로 세팅
        Page<Object[]> result; // object의 결과는 Page객체에 담고
        String[] type = pageRequestDTO.getType (); // 키워드 담고
        Sort sort = Sort.by (Sort.Direction.DESC, "bno"); // 정렬방식 담고.
        // SearchBaordRepositoryImply에서 정의한 Sort 정렬방식과 맞춰야 한다
        Pageable pageable = PageRequest.of (pageRequestDTO.getPage () - 1, pageRequestDTO.getSize (), sort);

        result = qnaBoardRepository.searchPage (type, pageRequestDTO.getKeyword (), pageable);
        log.info ("실행결과 : " + result);

        return new PageResultDTO<> (result, fn);
    }

/*    @Override
    public PageResultDTO<QnaBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        // 로그인한 사용자 정보를 세션에서 추출
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = userDetails.getMember();

        Function<Object[], QnaBoardDTO> fn = (en -> entityToDTO((QnaBoard)en[0], (Member)en[1], (Long)en[2]));

        // SearchBoardRepository에서 정의한 내용으로 세팅
        Page<Object[]> result; // object의 결과는 Page객체에 담고
        String[] type = pageRequestDTO.getType(); // 키워드 담고
        Sort sort = Sort.by(Sort.Direction.DESC, "bno"); // 정렬방식 담고.
        // SearchBaordRepositoryImply에서 정의한 Sort 정렬방식과 맞춰야 한다
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        if (member.getRole() == Role.ADMIN) {
            result = repository.searchPage(type, pageRequestDTO.getKeyword(), pageable);
        } else {
            result = repository.searchPageByWriter(type, member, pageRequestDTO.getKeyword(), pageable);
        }
        log.info("실행결과 : " + result);

        return new PageResultDTO<>(result, fn);
    }*/

/*
    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        
        qnaReplyRepository.deleteByBno(bno); //댓글부터 삭제
        
        repository.deleteById(bno); //이후 본 글 삭제
    }*/
} //class
