package org.ezone.room.service;

import org.ezone.room.dto.*;
import org.ezone.room.entity.*;
import org.ezone.room.repository.MemberRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AccommodationService {

    Long register(AccommodationDTO dto); //등록
    AccommodationDTO findAccommodationByMemberId(String memberId); // 멤버 찾아오기
    void modify(AccommodationDTO accommodationDTO); //수정
    void remove(Long ano); //삭제
    AccommodationDTO findByAno(Long ano); // 숙소찾기
    List<AccommodationImgDTO> findAccommodationWithImages();// 이미지
    public List<ImgDTO> getImgList(Long ano);
    public List<Object[]> list(LocalDate StartDate, LocalDate EndDate);
    public PageResultDTO<AccommodationDTO, Object[]> searchAccommodationList
            (PageRequestDTO pageRequestDTO, String keyword, String category, String region,
             LocalDate startDate, LocalDate endDate, Integer inputedMinPrice, Integer inputedMaxPrice);


    default AccommodationEntity dtoToEntity(AccommodationDTO dto, MemberRepository memberRepository){

        Member member = memberRepository.findByEmail(dto.getEmail());
        // Member객체 생성 - 리포지토리 실행결과를 담는다.

        AccommodationEntity accommodationEntity = AccommodationEntity.builder()
                .category(dto.getCategory())
                .name(dto.getName())
                .tel(dto.getTel())
                .email(member.getEmail())
                .adminname(dto.getAdminname())
                .region(dto.getRegion())
                .postal(dto.getPostal())
                .address(dto.getAddress())
                .content(dto.getContent())
                .intro(dto.getIntro())
                .member(member)
                .build();

        return accommodationEntity;
    }

    default AccommodationDTO entityToDto(AccommodationEntity accommodationEntity, Member member) {

        AccommodationDTO accommodationDTO = AccommodationDTO.builder()
                .email(member.getEmail())
                .ano(accommodationEntity.getAno())
                .name(accommodationEntity.getName())
                .adminname(accommodationEntity.getAdminname())
                .grade(accommodationEntity.getGrade())
                .address(accommodationEntity.getAddress())
                .postal(accommodationEntity.getPostal())
                .category(accommodationEntity.getCategory())
                .region(accommodationEntity.getRegion())
                .tel(accommodationEntity.getTel())
                .reviewcount(accommodationEntity.getReviewcount())
                .intro(accommodationEntity.getIntro())
                .count(accommodationEntity.getCount())
                .content(accommodationEntity.getContent())
                .build();
        return accommodationDTO;
    }

    default AccommodationDTO entityToDtoSearch(AccommodationEntity accommodationEntity, RoomEntity roomEntity, Integer minPrice) {

        RoomDTO roomDTO = RoomDTO.builder().room_name(roomEntity.getRoom_name()).build();

        AccommodationDTO accommodationDTO = AccommodationDTO.builder()
                .ano(accommodationEntity.getAno())
                .name(accommodationEntity.getName())
                .adminname(accommodationEntity.getAdminname())
                .grade(accommodationEntity.getGrade())
                .address(accommodationEntity.getAddress())
                .postal(accommodationEntity.getPostal())
                .intro(accommodationEntity.getIntro())
                .category(accommodationEntity.getCategory())
                .region(accommodationEntity.getRegion())
                .tel(accommodationEntity.getTel())
                .count(accommodationEntity.getCount())
                .content(accommodationEntity.getContent())
                .reviewcount(accommodationEntity.getReviewcount())
                .minPrice(minPrice)
                .roomDTO(roomDTO)
                .build();
        return accommodationDTO;
    }
}
