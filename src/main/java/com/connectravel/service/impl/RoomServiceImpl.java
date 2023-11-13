package com.connectravel.service.impl;

import com.connectravel.domain.dto.AccommodationDTO;
import com.connectravel.domain.dto.ImgDTO;
import com.connectravel.domain.dto.RoomDTO;
import com.connectravel.domain.entity.Accommodation;
import com.connectravel.domain.entity.Room;
import com.connectravel.domain.entity.RoomImg;
import com.connectravel.repository.AccommodationRepository;
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

    @Override
    @Transactional
    public Long registerRoom(RoomDTO roomDTO) {
        Accommodation accommodation = accommodationRepository.findById(roomDTO.getAccommodationDTO().getAno())
                .orElseThrow(() -> new EntityNotFoundException("숙소를 찾을 수 없습니다."));

        Room room = dtoToEntity(roomDTO);

        accommodation.addRoom(room);

        accommodationRepository.save(accommodation);

        Room savedRoom = roomRepository.save(room);

        // 저장된 Room의 ID 반환
        return savedRoom.getRno();
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

    @Transactional(readOnly = true)
    public List<RoomDTO> listRoomsByAccommodation(Long ano) {
        return roomRepository.findByAccommodationAno(ano).stream()
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