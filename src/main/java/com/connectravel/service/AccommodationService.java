package com.connectravel.service;

import com.connectravel.dto.AccommodationDTO;
import com.connectravel.entity.Accommodation;

public interface AccommodationService {

    Accommodation registerAccommodation(AccommodationDTO accommodationDTO);
    // Test에서 setUp() 대신 사용 중

    AccommodationDTO modifyAccommodationDetails(AccommodationDTO accommodationDTO);
    // 숙소의 상세 정보를 수정(현재는 이름, 주소만 수정 가능)

    AccommodationDTO getAccommodationDetails(Long accommodationAno);
    // 숙소의 상세 정보를 가져옴(옵션, 이미지 X)


}
