package com.connectravel.service;

import com.connectravel.dto.AccommodationDTO;
import com.connectravel.dto.RoomDTO;
import com.connectravel.entity.Accommodation;
import com.connectravel.entity.Room;

public interface AccommodationService {

    AccommodationDTO modifyAccommodationDetails(AccommodationDTO accommodationDTO);

    default AccommodationDTO entityToDto(Accommodation accommodation) {

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
                .build();
        return accommodationDTO;
    }

    default AccommodationDTO entityToDtoSearch(Accommodation accommodation, Room room, Integer minPrice) {

        RoomDTO roomDTO = RoomDTO.builder().roomName(room.getRoomName()).build();

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
                .roomDTO(roomDTO)
                .build();
        return accommodationDTO;
    }

}
