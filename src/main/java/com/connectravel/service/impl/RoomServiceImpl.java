package com.connectravel.service.impl;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.dto.ImgDTO;
import com.connectravel.domain.dto.RoomDTO;
import com.connectravel.domain.entity.Accommodation;
import com.connectravel.domain.entity.Room;
import com.connectravel.domain.entity.RoomImg;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.RoomImgRepository;
import com.connectravel.repository.RoomRepository;
import com.connectravel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final AccommodationRepository accommodationRepository;
    private final RoomImgRepository roomImgRepository;

    @Override
    @Transactional
    public RoomDTO registerRoom(RoomDTO roomDTO) {

        // 숙소 ID로 숙소 정보 조회
        Accommodation accommodation = accommodationRepository.findById(roomDTO.getAccommodationDTO().getAno())
                .orElseThrow(() -> new EntityNotFoundException("숙소를 찾을 수 없습니다."));

        // RoomDTO로부터 Room 엔티티 생성
        Room room = dtoToEntity(roomDTO);

        // Accommodation에 Room 추가
        accommodation.addRoom(room);

        // accommodation 객체에 변경사항을 저장
        accommodationRepository.save(accommodation);

        // Room 저장 (JPA가 자동으로 생성한 ID를 포함하여)
        Room savedRoom = roomRepository.save(room);

        // Room 엔티티를 RoomDTO로 변환하여 반환
        return entityToDTO(savedRoom);
    }

    @Override
    @Transactional
    public ImgDTO addRoomImage(Long roomId, ImgDTO imgDTO) {

        // 방 정보 조회
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("방을 찾을 수 없습니다."));

        // 이미지 객체 생성
        RoomImg roomImg = RoomImg.builder()
                .imgFile(imgDTO.getImgFile())
                .room(room)
                .build();

        // 이미지 저장
        RoomImg savedImg = roomImgRepository.save(roomImg);

        // 저장된 이미지를 DTO로 변환하여 반환
        return imgToDTO(savedImg);
    }

    @Override
    @Transactional
    public RoomDTO modifyRoom(Long rno, RoomDTO roomDTO) {

        Room room = roomRepository.findById(rno)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room Id: " + rno));

        room.updateRoomDetails(roomDTO);

        Room updatedRoom = roomRepository.save(room);

        return entityToDTO(updatedRoom);
    }

    @Override
    @Transactional
    public void removeRoom(Long rno) {

        Room room = roomRepository.findById(rno)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room Id: " + rno));
        roomRepository.delete(room);

    }

    @Override
    @Transactional(readOnly = true)
    public RoomDTO getRoom(Long rno) {

        Room room = roomRepository.findById(rno)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room Id: " + rno));

        return entityToDTO(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDTO> listAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO entityToDTO(Room room) {

        AccommodationDTO accommodationDTO = new AccommodationDTO();
        accommodationDTO.setAno(room.getAccommodation().getAno());

        return RoomDTO.builder()
                .rno(room.getRno())
                .roomName(room.getRoomName())
                .price(room.getPrice())
                .minimumOccupancy(room.getMinimumOccupancy())
                .maximumOccupancy(room.getMaximumOccupancy())
                .operating(room.isOperating())
                .content(room.getContent())
                .accommodationDTO(accommodationDTO)
                .build();
    }

    @Override
    public Room dtoToEntity(RoomDTO roomDTO) {
        Accommodation accommodation = accommodationRepository.findById(roomDTO.getAccommodationDTO().getAno())
                .orElseThrow(() -> new EntityNotFoundException("숙소를 찾을 수 없습니다."));

        return Room.builder()
                .roomName(roomDTO.getRoomName())
                .price(roomDTO.getPrice())
                .minimumOccupancy(roomDTO.getMinimumOccupancy())
                .maximumOccupancy(roomDTO.getMaximumOccupancy())
                .operating(roomDTO.isOperating())
                .content(roomDTO.getContent())
                .accommodation(accommodation)
                .build();
    }

    private ImgDTO imgToDTO(RoomImg roomImg) {
        return ImgDTO.builder()
                .ino(roomImg.getIno())
                .imgFile(roomImg.getImgFile())
                .room(entityToDTO(roomImg.getRoom())) // Room 정보를 RoomDTO로 변환하여 설정
                .build();
    }

}