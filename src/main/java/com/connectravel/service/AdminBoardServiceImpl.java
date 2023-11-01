package com.connectravel.service;

import com.connectravel.dto.AdminBoardDTO;
import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.entity.AdminBoard;
import com.connectravel.entity.Member;
import com.connectravel.repository.AdminBoardImgRepository;
import com.connectravel.repository.AdminBoardRepository;
import com.connectravel.repository.AdminReplyRepository;
import com.connectravel.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
@Log4j2
public class AdminBoardServiceImpl implements AdminBoardService {

    private final AdminBoardRepository repository;
    private final AdminReplyRepository adminReplyRepository;
    private final AdminBoardImgRepository adminBoardImgRepository;
    private final MemberRepository memberRepository; // Member 객체를 불러오기 위해 필요

    @Autowired
    private ModelMapper modelMapper;


    @Override
    @Transactional
    public Long register(AdminBoardDTO dto) {

        log.info("테스트 : " + dto);

        AdminBoard adminBoard = dtoToEntity(dto, memberRepository);

        repository.save(adminBoard);

        return adminBoard.getBno();
    }

    @Override
    public AdminBoardDTO get(Long bno) {
        Object result = repository.getBoardByBno(bno);

        Object[] arr = (Object[]) result;

        return entityToDTO((AdminBoard) arr[0], (Member) arr[1], (Long) arr[2]);
        // { {bno, writer, contet, category... }, {id, email,...}, {1}}
        //AdminBoard, Member 엔티티와 댓글의 수(Long)를 가져오는 getBoardByBno메서드 이용 처리
    }


    @Override
    public PageResultDTO<AdminBoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO, String category) {

        Function<Object[], AdminBoardDTO> fn = (en -> entityToDTO((AdminBoard) en[0], (Member) en[1], (Long) en[2]));

        //SearchBoardRepository에서 정의한 내용으로 세팅
        Page<Object[]> result; //Object결과를 Page객체에 담고
        String[] type = pageRequestDTO.getType(); //검색 종류 담고
        Sort sort = Sort.by(Sort.Direction.DESC, "bno"); //정렬방식 담음
        // SearchBaordRepositoryImpl에서 정의한 Sort 정렬방식과 맞춰야 한다
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        //조회는 모든 사용자가 다 볼 수 있게 설정(Role에 상관없이)
        result = repository.searchPageAdminBaord(type, category, pageRequestDTO.getKeyword(), pageable);

        log.info("실행결과 : " + result);

        return new PageResultDTO<>(result, fn);
    }

    @Transactional
    @Override
    public void modify(AdminBoardDTO adminBoardDTO) {

        AdminBoard adminBoard = repository.getOne(adminBoardDTO.getBno()); //adminRepository에서 Board객체를 받아
        //필요한 순간까지 로딩을 지연하는 getOne메서드 이용

        adminBoard.changeTitle(adminBoardDTO.getTitle()); //Board객체의 제목 수정
        adminBoard.changeContent(adminBoardDTO.getContent()); //Board객체의 내용 수정

        repository.save(adminBoard); //수정된 객체를 repository에 저장
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) {

        adminReplyRepository.deleteByBno(bno); //댓글부터 삭제

        repository.deleteById(bno); //이후 본 글 삭제
    }

    public List<ImgDTO> getImgList(Long bno) {
        List<ImgDTO> list = new ArrayList<>();
        AdminBoard entity = repository.findById(bno).get();
        adminBoardImgRepository.GetImgbybno(entity).forEach(i -> { //이미지는 룸을 참조하고 있다 그러니 이미지가 참조하는 룸에 해당하는 모든 이미지를 불러온다
            ImgDTO imgDTO = modelMapper.map(i, ImgDTO.class); //dto변환
            list.add(imgDTO); // list화
        });
        return list;
    }

}
