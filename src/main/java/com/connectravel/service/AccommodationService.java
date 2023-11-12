package com.connectravel.service;

import com.connectravel.dto.AccommodationDTO;
import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.entity.Accommodation;

import java.time.LocalDate;
import java.util.List;

public interface AccommodationService {

    Accommodation registerAccommodation(AccommodationDTO accommodationDTO);
    // Test에서 setUp() 대신 사용 중

    AccommodationDTO modifyAccommodationDetails(AccommodationDTO accommodationDTO);
    // 숙소의 상세 정보를 수정(현재는 이름, 주소만 수정 가능)

    AccommodationDTO getAccommodationDetails(Long accommodationAno);
    // 숙소의 상세 정보를 가져옴(옵션, 이미지 X)

    PageResultDTO<AccommodationDTO, Object[]> searchAccommodationList
            (PageRequestDTO pageRequestDTO, String keyword, String category, String region,
             LocalDate startDate, LocalDate endDate, Integer inputedMinPrice, Integer inputedMaxPrice);

    List<ImgDTO> getImgList(Long ano);

}
