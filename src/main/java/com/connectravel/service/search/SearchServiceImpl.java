package com.connectravel.service.search;

import com.connectravel.dto.search.AccommodationSearchDTO;
import com.connectravel.entity.Accommodation;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.search.SearchRepository;
import com.connectravel.repository.search.SearchRepositoryImpl;
import com.connectravel.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final AccommodationRepository accommodationRepository;

    @Override
    public List<AccommodationSearchDTO> searchByRegion(String region) {
        List<Accommodation> accommodations = accommodationRepository.findByRegion(region);
        return accommodations.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccommodationSearchDTO> searchByOptions(String region, Set<Long> optionIds) {
        List<Accommodation> accommodations = accommodationRepository.findByOptions(optionIds);
        return accommodations.stream()
                .filter(accommodation -> accommodation.getRegion().equals(region))
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccommodationSearchDTO> searchByRoomCriteria(String region, int price, int minimumOccupancy, int maximumOccupancy, boolean operating) {
        List<Accommodation> accommodations = accommodationRepository.findByRoomCriteria(price, minimumOccupancy, maximumOccupancy, operating);
        return accommodations.stream()
                .filter(accommodation -> accommodation.getRegion().equals(region))
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccommodationSearchDTO> searchByRegionOptionsAndRoomCriteria(String region, Set<Long> optionIds, int price, int minimumOccupancy, int maximumOccupancy, boolean operating) {
        // 이 메서드는 위의 메서드를 조합하여 구현할 수 있습니다.
        // 예를 들어, 먼저 옵션에 따른 검색을 수행하고, 그 결과를 방 기준으로 필터링 할 수 있습니다.
        // 이 예시는 간단화를 위해 각 메서드를 개별적으로 호출하는 것으로 구현됩니다.
        return searchByOptions(region, optionIds).stream()
                .filter(dto -> dto.getPriceRange().endsWith(String.valueOf(price)) &&
                        // 가격, 인원수, 운영 여부에 대한 추가적인 필터링 로직 필요
                        // 예시 코드에서는 priceRange 필드와 매칭하는 간단한 예시를 제시합니다.
                        dto.isOperating() == operating)
                .collect(Collectors.toList());
    }

    private AccommodationSearchDTO entityToDTO(Accommodation accommodation) {
        // Entity에서 DTO로 변환하는 로직을 구현합니다.
        // 이 메서드는 Entity의 정보를 AccommodationSearchDTO로 매핑하는 로직을 포함해야 합니다.
        // 예시 코드에서는 실제 데이터를 사용하지 않는 간단한 변환을 보여줍니다.
        return AccommodationSearchDTO.builder()
                .ano(accommodation.getAno())
                .accommodationName(accommodation.getAccommodationName())
                .grade(accommodation.getGrade())
                .region(accommodation.getRegion())
                // 다른 필드들도 적절히 매핑합니다.
                .build();
    }

    // 필요한 경우 DTO로부터 Entity로 변환하는 메서드도 추가할 수 있습니다.
}