package com.connectravel.service.impl;

import com.connectravel.domain.dto.ImgDTO;
import com.connectravel.domain.dto.PageRequestDTO;
import com.connectravel.domain.dto.PageResultDTO;
import com.connectravel.domain.dto.TourBoardDTO;
import com.connectravel.domain.entity.TourBoard;
import com.connectravel.repository.TourBoardImgRepository;
import com.connectravel.repository.TourBoardRepository;
import com.connectravel.service.TourBoardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourBoardServiceImpl implements TourBoardService {

    private final ModelMapper modelMapper;

    private final TourBoardRepository tourBoardRepository;

    private final TourBoardImgRepository tourBoardImgRepository;

    @Override
    @Transactional
    public Long createTourBoard(TourBoardDTO dto) {

        TourBoard entity = dtoToEntity(dto);

        tourBoardRepository.save(entity);

        return entity.getTbno();
    }

    @Override
    public TourBoardDTO getTourBoard(Long tbno) {

        Optional<TourBoard> result = tourBoardRepository.findById(tbno);

        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    @Override
    public void updateTourBoard(TourBoardDTO dto) {

        Optional<TourBoard> result = tourBoardRepository.findById(dto.getTbno());

        if (result.isPresent()) {

            TourBoard entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            tourBoardRepository.save(entity);
        }
    }

    @Override
    public void deleteTourBoard(Long tbno) {
        tourBoardRepository.deleteById(tbno);
    }

    @Override
    public PageResultDTO<TourBoardDTO, Object[]> getPaginatedTourBoardList(PageRequestDTO pageRequestDTO, String[] type, String keyword, String category, String region) {

        Sort sort = Sort.by(Sort.Direction.DESC, "tbno");
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        Page<Object[]> result;
        if (pageRequestDTO.getKeyword() != null) {
            result = tourBoardRepository.searchTourBoard(type, pageRequestDTO.getKeyword(), category, region, pageable);
        }
        else {
            result = tourBoardRepository.searchTourBoard(type, keyword, category, region, pageable);
        }

        Function<Object[], TourBoardDTO> fn = (objectArr -> {
            TourBoard tourBoard = (TourBoard) objectArr[0];
            List<ImgDTO> imgDTOList = listTourBoardImages(tourBoard.getTbno()); // 이미지 DTO 리스트화

            // ImgDTO의 리스트에서 이미지 파일 이름만 추출하여 리스트 생성
            List<String> imgFileNames = imgDTOList.stream()
                    .map(ImgDTO::getImgFile)
                    .collect(Collectors.toList());

            TourBoardDTO tourBoardDTO = entityToDto(tourBoard); // TourBoard를 TourBoardDTO로 변환
            tourBoardDTO.setImgFiles(imgFileNames); // TourBoardDTO에 이미지 파일 이름 리스트 설정

            return tourBoardDTO;
        });

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public List<ImgDTO> listTourBoardImages(Long tbno) {

        List<ImgDTO> list = new ArrayList<>();

        TourBoard entity = tourBoardRepository.findById(tbno).get();
        tourBoardImgRepository.getImgByTourBoardTbno(entity).forEach(i -> {
            ImgDTO imgDTO = modelMapper.map(i, ImgDTO.class);
            list.add(imgDTO); // list화
        });

        return list;
    }

    private TourBoard dtoToEntity(TourBoardDTO dto) {

        return TourBoard.builder()
                .tbno(dto.getTbno())
                .postal(dto.getPostal())
                .address(dto.getAddress())
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(dto.getCategory())
                .region(dto.getRegion())
                .build();
    }

    private TourBoardDTO entityToDto(TourBoard entity) {

        return TourBoardDTO.builder()
                .tbno(entity.getTbno())
                .content(entity.getContent())
                .title(entity.getTitle())
                .postal(entity.getPostal())
                .region(entity.getRegion())
                .address(entity.getAddress())
                .category(entity.getCategory())
                .grade(entity.getGrade())
                .reviewCount(entity.getReviewCount())
                .regDate(entity.getRegTime())
                .modDate(entity.getModTime())
                .build();

    }

}