package com.connectravel.service.impl;

import com.connectravel.domain.dto.*;
import com.connectravel.domain.entity.Accommodation;
import com.connectravel.domain.entity.AccommodationImg;
import com.connectravel.domain.entity.Room;
import com.connectravel.repository.AccommodationImgRepository;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.service.SearchService;
import lombok.extern.log4j.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class SearchServiceImpl implements SearchService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationImgRepository accommodationImgRepository;

    @Override
    public PageResultDTO<AccommodationDTO, Object[]> searchAccommodationList(
            PageRequestDTO pageRequestDTO, String keyword, String accommodationType, String region,
            LocalDate startDate, LocalDate endDate, Integer inputedMinPrice, Integer inputedMaxPrice, Set<Long> optionIds) {

        log.info("Search Accommodation List: keyword={}, accommodationType={}, region={}, startDate={}, endDate={}, inputedMinPrice={}, inputedMaxPrice={}, optionIds={}",
                keyword, accommodationType, region, startDate, endDate, inputedMinPrice, inputedMaxPrice, optionIds);

        Sort sort = Sort.by(Sort.Direction.DESC, "ano");
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        Page<Accommodation> accommodations = accommodationRepository.searchFilteredAccommodations(
                region, optionIds, startDate, endDate, inputedMinPrice, inputedMaxPrice, pageable);

        Page<Object[]> result = accommodationRepository.searchPageAccommodation(
                pageRequestDTO.getType(), keyword, accommodationType, region, startDate, endDate,
                inputedMinPrice, inputedMaxPrice, pageable);

        Function<Object[], AccommodationDTO> fn = (objectArr -> {
            Accommodation accommodation = (Accommodation) objectArr[0];
            Room room = (Room) objectArr[1];
            Integer minPrice = (Integer) objectArr[2];
            AccommodationDTO accommodationDTO = entityToDtoSearch(accommodation, room, minPrice);
            List<ImgDTO> imgDTOList = getImgList(accommodation.getAno());
            List<String> imgFiles = imgDTOList.stream().map(ImgDTO::getImgFile).collect(Collectors.toList());
            accommodationDTO.setImgFiles(imgFiles); // 올바른 메서드를 사용하여 이미지 파일 경로 설정
            return accommodationDTO;
        });

        return new PageResultDTO<>(result, fn);
    }

    private List<ImgDTO> getImgList(Long ano) {

        List<ImgDTO> imgDTOList = new ArrayList<>();

        List<AccommodationImg> accommodationImgList = accommodationImgRepository.findByAccommodationAno(ano);
        for (AccommodationImg accommodationImg : accommodationImgList) {
            ImgDTO imgDTO = new ImgDTO();
            imgDTO.setIno(accommodationImg.getIno());
            imgDTO.setImgFile(accommodationImg.getImgFile());
            imgDTOList.add(imgDTO);
        }

        return imgDTOList;
    }


    private AccommodationDTO entityToDtoSearch(Accommodation accommodation, Room room, Integer minPrice) {

        RoomDTO roomDTO = RoomDTO.builder().roomName(room.getRoomName()).build();
        List<RoomDTO> roomDTOList = new ArrayList<>();
        roomDTOList.add(roomDTO);

        AccommodationDTO accommodationDTO = AccommodationDTO.builder()
                .ano(accommodation.getAno())
                .accommodationName(accommodation.getAccommodationName())
                .sellerName(accommodation.getMember().getUsername()) // 관리자 이름
                .sellerEmail(accommodation.getMember().getEmail()) // 관리자 이메일
                .address(accommodation.getAddress())
                .postal(accommodation.getPostal())
                .accommodationType(accommodation.getAccommodationType())
                .region(accommodation.getRegion())
                .tel(accommodation.getTel())
                .intro(accommodation.getIntro())
                .count(accommodation.getCount())
                .content(accommodation.getContent())
                .minPrice(minPrice)
                .roomDTO(roomDTOList)
                .build();

        return accommodationDTO;
    }

}