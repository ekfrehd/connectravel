package com.connectravel.service.impl;

import com.connectravel.domain.dto.*;
import com.connectravel.domain.entity.*;
import com.connectravel.repository.AccommodationImgRepository;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.OptionRepository;
import com.connectravel.service.AccommodationService;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationImgRepository accommodationImgRepository;
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Accommodation registerAccommodation(AccommodationDTO accommodationDTO) {

        String sellerEmail = accommodationDTO.getSellerEmail();
        Member member = memberRepository.findByEmail(sellerEmail);

        if (member == null) {
            throw new EntityNotFoundException("Member not found or not authorized");
        }

        // Accommodation 엔티티 생성
        Accommodation accommodation = Accommodation.builder()
                .accommodationName(accommodationDTO.getAccommodationName())
                .postal(accommodationDTO.getPostal())
                .sellerName(member.getNickName())
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
    public PageResultDTO<AccommodationDTO, Object[]> searchAccommodationList(
            PageRequestDTO pageRequestDTO, String keyword, String category, String region,
            LocalDate startDate, LocalDate endDate, Integer inputedMinPrice, Integer inputedMaxPrice) {

        Sort sort = Sort.by(Sort.Direction.DESC, "ano");
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        Page<Object[]> result = accommodationRepository.searchPageAccommodation(
                pageRequestDTO.getType(), keyword, category, region, startDate, endDate,
                inputedMinPrice, inputedMaxPrice, pageable);

        Function<Object[], AccommodationDTO> fn = (objectArr -> {
            Accommodation accommodation = (Accommodation) objectArr[0];
            Room room = (Room) objectArr[1];
            Integer minPrice = (Integer) objectArr[2];
            AccommodationDTO accommodationDTO = entityToDtoSearch(accommodation, room, minPrice);
            List<ImgDTO> imgDTOList = getImgList(accommodation.getAno());
            List<String> imgFiles = imgDTOList.stream().map(ImgDTO::getImgFile).collect(Collectors.toList());
            accommodationDTO.setImgFiles(imgFiles); // 올바른 메서드를 사용하여 이미지 파일 경로 설정
            return accommodationDTO;
        });

        return new PageResultDTO<>(result, fn);
    }


    public List<ImgDTO> getImgList(Long ano) {
        List<ImgDTO> imgDTOList = new ArrayList<>();

        List<AccommodationImg> accommodationImgList = accommodationImgRepository.findByAccommodationAno(ano);
        for (AccommodationImg accommodationImg : accommodationImgList) {
            ImgDTO imgDTO = new ImgDTO();
            imgDTO.setIno(accommodationImg.getIno());
            imgDTO.setImgFile(accommodationImg.getImgFile());
            imgDTOList.add(imgDTO);
        }

        return imgDTOList;
    }

    /* 변환 메서드 */
    private AccommodationDTO entityToDto(Accommodation accommodation) {

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
                .sellerName(accommodation.getMember().getNickName()) // 관리자 이름
                .sellerEmail(accommodation.getMember().getEmail()) // 관리자 이메일
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

    private AccommodationDTO entityToDtoSearch(Accommodation accommodation, Room room, Integer minPrice) {

        RoomDTO roomDTO = RoomDTO.builder().roomName(room.getRoomName()).build();
        List<RoomDTO> roomDTOList = new ArrayList<>();
        roomDTOList.add(roomDTO);

        AccommodationDTO accommodationDTO = AccommodationDTO.builder()
                .ano(accommodation.getAno())
                .accommodationName(accommodation.getAccommodationName())
                .sellerName(accommodation.getMember().getNickName()) // 관리자 이름
                .sellerEmail(accommodation.getMember().getEmail()) // 관리자 이메일
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