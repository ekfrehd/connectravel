package com.connectravel.service;

import com.connectravel.domain.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface AccommodationService {

    Long registerAccommodation(AccommodationDTO accommodationDTO);

    AccommodationDTO modifyAccommodationDetails(AccommodationDTO accommodationDTO);

    AccommodationDTO findByAno(Long ano);

    AccommodationDTO findAccommodationByMemberId(Long memberId);

    List<ImgDTO> getImgList(Long ano);

    PageResultDTO<AccommodationDTO, Object[]> searchAccommodationList
            (PageRequestDTO pageRequestDTO, String keyword, String category, String region,
             LocalDate startDate, LocalDate endDate, Integer inputedMinPrice, Integer inputedMaxPrice);


    /* 숙소 검색 기능 */
  /*  PageResultDTO<AccommodationDTO, Object[]> searchAccommodations
            (AccommodationSearchDTO searchDTO, PageRequestDTO pageRequestDTO);
*/
}