package com.connectravel.service;

import com.connectravel.dto.AccommodationDTO;
import com.connectravel.dto.RoomDTO;
import com.connectravel.entity.Accommodation;
import com.connectravel.entity.Member;
import com.connectravel.entity.Room;
import com.connectravel.repository.AccommodationRepository;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private AccommodationRepository accommodationRepository;

    @Override
    public AccommodationDTO modifyAccommodationDetails(AccommodationDTO accommodationDTO) {
        // 숙소 정보 수정을 위해 DTO에서 필요한 정보 추출
        Long accommodationId = accommodationDTO.getAno();
        String newName = accommodationDTO.getName();
        String newAddress = accommodationDTO.getAddress();
        // 필요한 다른 속성들도 추출

        // 숙소 정보 유효성 검사 및 엔티티 조회
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation not found"));

        // 엔티티에 수정된 정보 적용
        accommodation.setName(newName);
        accommodation.setAddress(newAddress);
        // 다른 수정 작업도 수행

        // 변경사항을 데이터베이스에 저장
        accommodationRepository.save(accommodation);

        // 수정된 숙소 정보를 다시 DTO로 변환하여 반환
        return entityToDto(accommodation);
    }



}
