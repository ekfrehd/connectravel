package com.connectravel.service;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.dto.ImgDTO;

import java.util.List;

public interface AccommodationService {

    Long registerAccommodation(AccommodationDTO accommodationDTO);

    AccommodationDTO modifyAccommodationDetails(AccommodationDTO accommodationDTO);

    AccommodationDTO findByAno(Long ano);

    AccommodationDTO findAccommodationByMemberId(Long memberId);

    List<ImgDTO> getImgList(Long ano);

}