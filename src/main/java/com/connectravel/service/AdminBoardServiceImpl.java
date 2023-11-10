package com.connectravel.service;

import com.connectravel.dto.AdminBoardDTO;
import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.entity.AdminBoard;
import com.connectravel.entity.Member;
import com.connectravel.repository.AdminBoardImgRepository;
import com.connectravel.repository.AdminBoardRepository;
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

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
@Log4j2
public class AdminBoardServiceImpl implements AdminBoardService {

    private final AdminBoardRepository adminBoardRepository;
    private final AdminBoardImgRepository adminBoardImgRepository;
    private final MemberRepository memberRepository; // Member 객체를 불러오기 위해 필요

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public Long registerAdminBoard(AdminBoardDTO dto) {

        log.info("테스트 : " + dto);

        AdminBoard adminBoard = dtoToEntity(dto, memberRepository);

        adminBoardRepository.save(adminBoard);

        return adminBoard.getAbno();
    }

    @Override
    public AdminBoardDTO getAdminBoard(Long abno) {
        Object result = adminBoardRepository.getBoardByAbno(abno);

        Object[] arr = (Object[]) result;

        return entityToDTO((AdminBoard) arr[0], (Member) arr[1], (Long) arr[2]);
    }

    @Override
    @Transactional
    public void updateAdminBoard(AdminBoardDTO adminBoardDTO) {

        AdminBoard adminBoard = adminBoardRepository.getOne(adminBoardDTO.getAbno()); //adminRepository에서 Board객체를 받아
        //필요한 순간까지 로딩을 지연하는 getOne메서드 이용

        adminBoard.changeTitle(adminBoardDTO.getTitle()); //Board객체의 제목 수정
        adminBoard.changeContent(adminBoardDTO.getContent()); //Board객체의 내용 수정

        adminBoardRepository.save(adminBoard); //수정된 객체를 repository에 저장
    }

    @Override // 게시글 삭제
    @Transactional
    public void deleteAdminBoard(Long abno) {
        adminBoardRepository.deleteById(abno); //이후 본 글 삭제
    }

    @Override
    public PageResultDTO<AdminBoardDTO, Object[]> getPaginatedAdminBoardList(PageRequestDTO pageRequestDTO, String category) {

        Function<Object[], AdminBoardDTO> fn = (en -> entityToDTO((AdminBoard) en[0], (Member) en[1], (Long) en[2]));

        //SearchBoardRepository에서 정의한 내용으로 세팅
        Page<Object[]> result; //Object결과를 Page객체에 담고
        String[] type = pageRequestDTO.getType(); //검색 종류 담고
        Sort sort = Sort.by(Sort.Direction.DESC, "bno"); //정렬방식 담음
        // SearchBaordRepositoryImpl에서 정의한 Sort 정렬방식과 맞춰야 한다
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        //조회는 모든 사용자가 다 볼 수 있게 설정(Role에 상관없이)
        result = adminBoardRepository.searchPageAdminBoard(type, category, pageRequestDTO.getKeyword(), pageable);

        log.info("실행결과 : " + result);

        return new PageResultDTO<>(result, fn);
    }

    public List<ImgDTO> getAdminBoardImgList(Long abno) {
        List<ImgDTO> list = new ArrayList<>();
        AdminBoard entity = adminBoardRepository.findById(abno)
                        .orElseThrow(() -> new EntityNotFoundException("AdminBoard not found"));

        adminBoardImgRepository.getImgByAbno(entity).forEach(i -> {
            ImgDTO imgDTO = modelMapper.map(i, ImgDTO.class);
            list.add(imgDTO);
        });
        return list;
    }

    private AdminBoard dtoToEntity(AdminBoardDTO dto, MemberRepository memberRepository) {

        Member member = memberRepository.findByEmail(dto.getWriterEmail())
                .orElseThrow(() -> new EntityNotFoundException("Member with email " + dto.getWriterEmail() + " not found"));

        AdminBoard adminBoard = AdminBoard.builder().abno(dto.getAbno()).title(dto.getTitle()).content(dto.getContent()).category(dto.getCategory()).member(member).build();
        //상단에서 생성한 Member객체 활용 Board객체 생성

        return adminBoard;
    }

    private AdminBoardDTO entityToDTO(AdminBoard adminBoard, Member member, Long adminReplyCount) {

        AdminBoardDTO adminBoardDTO = AdminBoardDTO.builder().abno(adminBoard.getAbno()).title(adminBoard.getTitle()).content(adminBoard.getContent()).regDate(adminBoard.getRegTime()).modDate(adminBoard.getModTime()).category(adminBoard.getCategory()).writerEmail(member.getEmail()).writerName(member.getNickName()).replyCount(adminReplyCount.intValue()).build();

        return adminBoardDTO;
    }
}
