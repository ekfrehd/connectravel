package org.ezone.room.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.dto.*;
import org.ezone.room.entity.TourBoard;
import org.ezone.room.repository.TourBaordImgRepository;
import org.ezone.room.repository.TourRepository;

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
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class TourBoardServiceImpl implements TourBoardService{

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourBaordImgRepository tourBaordImgRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public Long register(TourBoardDTO dto) {

        log.info("DTO.................");
        log.info(dto);

        TourBoard entity = dtoToEntity(dto);

        log.info(entity);

        tourRepository.save(entity);

        return entity.getTbno();
    }

    @Override
    public TourBoardDTO read(Long gno) {

        Optional<TourBoard> result = tourRepository.findById(gno);

        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    @Override
    public void remove(Long gno) {

        tourRepository.deleteById(gno);
    }

    @Override
    public void modify(TourBoardDTO dto) {

        //업데이트 하는 항목은 제목, 내용
        Optional<TourBoard> result = tourRepository.findById(dto.getTbno());

        if (result.isPresent()) {

            TourBoard entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            tourRepository.save(entity);
        }
    }

    @Override
    public PageResultDTO<TourBoardDTO, Object[]> getList
            (PageRequestDTO pageRequestDTO, String[] type, String keyword, String category, String region) {
        Sort sort = Sort.by(Sort.Direction.DESC, "tbno");
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        Page<Object[]> result;
        if (pageRequestDTO.getKeyword() != null) {
            result = tourRepository.searchTourBoard(type, pageRequestDTO.getKeyword(), category, region, pageable);
        } else {
            result = tourRepository.searchTourBoard(type, keyword, category, region, pageable);
        }

        Function<Object[], TourBoardDTO> fn = (objectArr -> {
            TourBoard tourBoard = (TourBoard) objectArr[0];
            List<ImgDTO> imgDTOList = getImgList(tourBoard.getTbno()); // 이미지 리스트화 게시물 번호별 -
            TourBoardDTO tourBoardDTO = entityToDto(tourBoard); // 게시물 변화
            tourBoardDTO.setImgDTOList(imgDTOList); // 게시물 리스트화

            return tourBoardDTO;
        });

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public List<ImgDTO> getImgList(Long tbno) {
        List<ImgDTO> list = new ArrayList<>();
        TourBoard entity = tourRepository.findById(tbno).get();
        tourBaordImgRepository.GetImgbyTourBoardId(entity).forEach(i -> { //이미지는 룸을 참조하고 있다 그러니 이미지가 참조하는 룸에 해당하는 모든 이미지를 불러온다
            ImgDTO imgDTO = modelMapper.map(i,ImgDTO.class); //dto변환
            list.add(imgDTO); // list화
        });
        return list;
    }

}