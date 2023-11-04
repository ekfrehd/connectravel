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

    Accommodation registerAccommodation(AccommodationDTO accommodationDTO);

    AccommodationDTO modifyAccommodationDetails(AccommodationDTO accommodationDTO);
    // 숙소의 상세 정보를 수정(현재는 이름, 주소만 수정 가능)

    AccommodationDTO getAccommodationDetails(Long accommodationAno);
    // 숙소의 상세 정보를 가져옴(옵션, 이미지 X)

    AccommodationDTO entityToDto(Accommodation accommodation);

    AccommodationDTO entityToDtoSearch(Accommodation accommodation, Room room, Integer minPrice);


}
