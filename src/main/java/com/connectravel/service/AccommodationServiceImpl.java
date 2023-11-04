package com.connectravel.service;

import com.connectravel.dto.AccommodationDTO;
import com.connectravel.dto.OptionDTO;
import com.connectravel.dto.RoomDTO;
import com.connectravel.entity.*;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.OptionRepository;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;

    @Override
    @Transactional
    public Accommodation registerAccommodation(AccommodationDTO accommodationDTO) {
        // Member 찾기
        Member member = memberRepository.findByEmail(accommodationDTO.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Member not found or not authorized"));

        // Accommodation 엔티티 생성
        Accommodation accommodation = Accommodation.builder()
                .accommodationName(accommodationDTO.getAccommodationName())
                .postal(accommodationDTO.getPostal())
                .sellerName(member.getName()) // Since it's a 1:1 relation, we can get the name directly
                .address(accommodationDTO.getAddress())
                .count(accommodationDTO.getCount())
                .region(accommodationDTO.getRegion())
                .tel(accommodationDTO.getTel())
                .content(accommodationDTO.getContent())
                .intro(accommodationDTO.getIntro())
                .accommodationType(accommodationDTO.getAccommodationType())
                .member(member)
                .build();

        // 이미지 추가
        for (String imgFile : accommodationDTO.getImgFiles()) {
            AccommodationImg img = new AccommodationImg();
            img.setImgFile(imgFile);
            accommodation.addImage(img);
        }

        // 옵션 추가
        for (OptionDTO optionDTO : accommodationDTO.getOptionDTO()) {
            Option option = optionRepository.findByOptionName(optionDTO.getOptionName())
                    .orElseThrow(() -> new EntityNotFoundException("Option not found"));
            AccommodationOption accommodationOption = new AccommodationOption();
            accommodationOption.setOption(option);
            accommodation.addAccommodationOption(accommodationOption);
        }

        // Accommodation 저장
        return accommodationRepository.save(accommodation);
    }

    @Override
    public AccommodationDTO modifyAccommodationDetails(AccommodationDTO accommodationDTO) {
        // 숙소 정보 수정을 위해 DTO에서 필요한 정보 추출
        Long accommodationId = accommodationDTO.getAno();
        String newName = accommodationDTO.getAccommodationName();
        String newAddress = accommodationDTO.getAddress();
        // 이름, 주소 외 다른 것도 수정할 수 있게 할거면 여기에 추가

        // 숙소 정보 유효성 검사 및 엔티티 조회
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation not found"));

        // 엔티티에 수정된 정보 적용
        accommodation.setAccommodationName(newName);
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


    @Override
    public AccommodationDTO entityToDto(Accommodation accommodation) {

        // 숙박 업소에 대한 이미지 정보만 변환
        List<String> imageFiles = accommodation.getImages().stream()
                .filter(image -> image.getAccommodation() != null)  // 숙박 업소에 연결된 이미지만 필터링
                .map(AccommodationImg::getImgFile)
                .collect(Collectors.toList());

        // 옵션 정보 변환
        List<OptionDTO> optionList = accommodation.getAccommodationOptions().stream()
                .map(option -> new OptionDTO(option.getAono(), option.getOption().getOptionName()))
                .collect(Collectors.toList());

        AccommodationDTO accommodationDTO = AccommodationDTO.builder()
                .ano(accommodation.getAno())
                .accommodationName(accommodation.getAccommodationName())
                .sellerName(accommodation.getMember().getName()) // 관리자 이름
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

    @Override
    public AccommodationDTO entityToDtoSearch(Accommodation accommodation, Room room, Integer minPrice) {

        RoomDTO roomDTO = RoomDTO.builder().roomName(room.getRoomName()).build();
        List<RoomDTO> roomDTOList = new ArrayList<>();
        roomDTOList.add(roomDTO);

        AccommodationDTO accommodationDTO = AccommodationDTO.builder()
                .ano(accommodation.getAno())
                .accommodationName(accommodation.getAccommodationName())
                .sellerName(accommodation.getMember().getName()) // 관리자 이름
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
