package com.connectravel.service.impl;

import com.connectravel.domain.dto.AdminBoardDTO;
import com.connectravel.domain.dto.ImgDTO;
import com.connectravel.domain.dto.PageRequestDTO;
import com.connectravel.domain.dto.PageResultDTO;
import com.connectravel.domain.entity.AdminBoard;
import com.connectravel.domain.entity.Member;
import com.connectravel.repository.AdminBoardImgRepository;
import com.connectravel.repository.AdminBoardRepository;
import com.connectravel.repository.MemberRepository;
import com.connectravel.service.AdminBoardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
public class AdminBoardServiceImpl implements AdminBoardService {

    private final AdminBoardRepository adminBoardRepository;

    private final AdminBoardImgRepository adminBoardImgRepository;

    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Long registerAdminBoard(AdminBoardDTO dto) {

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

        AdminBoard adminBoard = adminBoardRepository.getOne(adminBoardDTO.getAbno());

        adminBoard.changeTitle(adminBoardDTO.getTitle());
        adminBoard.changeContent(adminBoardDTO.getContent());

        adminBoardRepository.save(adminBoard);
    }

    @Override
    @Transactional
    public void deleteAdminBoard(Long abno) {
        adminBoardRepository.deleteById(abno);
    } // 게시글 삭제

    @Override
    public PageResultDTO<AdminBoardDTO, Object[]> getPaginatedAdminBoardList(PageRequestDTO pageRequestDTO, String category) {

        Function<Object[], AdminBoardDTO> fn = (en -> entityToDTO((AdminBoard) en[0], (Member) en[1], (Long) en[2]));

        Page<Object[]> result;
        String[] type = pageRequestDTO.getType();
        Sort sort = Sort.by(Sort.Direction.DESC, "abno");

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        result = adminBoardRepository.searchPageAdminBoard(type, category, pageRequestDTO.getKeyword(), pageable);

        return new PageResultDTO<>(result, fn);
    }

    @Override
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

    /* 변환 메서드 */
    private AdminBoard dtoToEntity(AdminBoardDTO dto, MemberRepository memberRepository) {

        Member member = memberRepository.findByEmail(dto.getWriterEmail());

        if (member == null) {
            throw new EntityNotFoundException("Member with email " + dto.getWriterEmail() + " not found");
        }

// Continue with the rest of your code using the 'member' object.


        AdminBoard adminBoard = AdminBoard.builder().abno(dto.getAbno()).title(dto.getTitle()).content(dto.getContent()).category(dto.getCategory()).member(member).build(); // 상단에서 생성한 Member객체 활용 Board객체 생성

        return adminBoard;
    }

    private AdminBoardDTO entityToDTO(AdminBoard adminBoard, Member member, Long adminReplyCount) {

        AdminBoardDTO adminBoardDTO = AdminBoardDTO.builder().abno(adminBoard.getAbno()).title(adminBoard.getTitle()).content(adminBoard.getContent()).regDate(adminBoard.getRegTime()).modDate(adminBoard.getModTime()).category(adminBoard.getCategory()).writerEmail(member.getEmail()).writerName(member.getNickName()).replyCount(adminReplyCount.intValue()).build();

        return adminBoardDTO;
    }

}