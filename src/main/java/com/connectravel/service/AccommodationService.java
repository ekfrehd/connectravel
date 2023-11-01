package com.connectravel.service;

import com.connectravel.dto.AccommodationDTO;
import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.OptionDTO;
import com.connectravel.dto.RoomDTO;
import com.connectravel.entity.Accommodation;
import com.connectravel.entity.AccommodationImg;
import com.connectravel.entity.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface AccommodationService {

    AccommodationDTO modifyAccommodationDetails(AccommodationDTO accommodationDTO);
    // 숙소의 상세 정보를 수정(현재는 이름, 주소만 수정 가능)

    AccommodationDTO getAccommodationDetails(Long accommodationAno);
    // 숙소의 상세 정보를 가져옴(옵션, 이미지 X)

    default AccommodationDTO entityToDto(Accommodation accommodation) {

        // 숙박 업소에 대한 이미지 정보만 변환
        List<String> imageFiles = accommodation.getImages().stream()
                .filter(image -> image.getAccommodation() != null)  // 숙박 업소에 연결된 이미지만 필터링
                .map(AccommodationImg::getImgFile)
                .collect(Collectors.toList());

        // 옵션 정보 변환
        List<OptionDTO> optionList = accommodation.getAccommodationOptions().stream()
                .map(option -> new OptionDTO(option.getOption().getOptionName(), option.getOption().getOptionCategory()))
                .collect(Collectors.toList());

        AccommodationDTO accommodationDTO = AccommodationDTO.builder()
                .ano(accommodation.getAno())
                .name(accommodation.getName())
                .adminName(accommodation.getMember().getName()) // 관리자 이름
                .email(accommodation.getMember().getEmail()) // 관리자 이메일
                .address(accommodation.getAddress())
                .postal(accommodation.getPostal())
                .accommodationType(accommodation.getAccommodationType())
                .region(accommodation.getRegion())
                .tel(accommodation.getTel())
                .intro(accommodation.getIntro())
                .count(accommodation.getCount())
                .content(accommodation.getContent())
                .imgFiles(imageFiles)
                .optionDTO(optionList)
                .build();

        return accommodationDTO;
    }

    default AccommodationDTO entityToDtoSearch(Accommodation accommodation, Room room, Integer minPrice) {

        RoomDTO roomDTO = RoomDTO.builder().roomName(room.getRoomName()).build();
        List<RoomDTO> roomDTOList = new ArrayList<>();
        roomDTOList.add(roomDTO);

        AccommodationDTO accommodationDTO = AccommodationDTO.builder()
                .ano(accommodation.getAno())
                .name(accommodation.getName())
                .adminName(accommodation.getMember().getName()) // 관리자 이름
                .email(accommodation.getMember().getEmail()) // 관리자 이메일
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
