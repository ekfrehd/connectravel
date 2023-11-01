package com.connectravel.service;

import com.connectravel.dto.AccommodationDTO;
import com.connectravel.entity.Accommodation;
import com.connectravel.repository.AccommodationRepository;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;


@Service
@Log4j2
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationRepository accommodationRepository;

    @Override
    public AccommodationDTO modifyAccommodationDetails(AccommodationDTO accommodationDTO) {
        // 숙소 정보 수정을 위해 DTO에서 필요한 정보 추출
        Long accommodationId = accommodationDTO.getAno();
        String newName = accommodationDTO.getName();
        String newAddress = accommodationDTO.getAddress();
        // 이름, 주소 외 다른 것도 수정할 수 있게 할거면 여기에 추가

        // 숙소 정보 유효성 검사 및 엔티티 조회
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation not found"));

        // 엔티티에 수정된 정보 적용
        accommodation.setName(newName);
        accommodation.setAddress(newAddress);

        // 변경사항을 데이터베이스에 저장
        accommodationRepository.save(accommodation);

        // 수정된 숙소 정보를 다시 DTO로 변환하여 반환
        return entityToDto(accommodation);
    }

    @Override
    public AccommodationDTO getAccommodationDetails(Long accommodationAno) {
        Accommodation accommodation = accommodationRepository.findById(accommodationAno)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation not found"));

        return entityToDto(accommodation);
    }


}
