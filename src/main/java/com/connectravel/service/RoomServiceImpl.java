package com.connectravel.service;

import com.connectravel.dto.RoomDTO;
import com.connectravel.entity.Accommodation;
import com.connectravel.entity.Room;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final AccommodationRepository accommodationRepository;

    @Override
    @Transactional
    public RoomDTO createRoom(RoomDTO roomDTO) {
        Long accommodationId = roomDTO.getAccommodationDTO().getAno();

        // 숙소 정보 조회
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation not found"));

        // Room 엔티티 생성
        Room room = Room.builder()
                .roomName(roomDTO.getRoomName())
                .price(roomDTO.getPrice())
                .operating(roomDTO.isOperating())
                .content(roomDTO.getContent())
                .accommodation(accommodation)
                .build();

        // Room 저장
        Room savedRoom = roomRepository.save(room);

        // Room 엔티티를 RoomDTO로 변환하여 반환
        return entityToDTO(savedRoom);
    }


    // Room 엔티티를 RoomDTO로 변환하는 메서드
    private RoomDTO entityToDTO(Room room) {
        return RoomDTO.builder()
                .rno(room.getRno())
                .roomName(room.getRoomName())
                .price(room.getPrice())
                .operating(room.isOperating())
                .content(room.getContent())
                .build();
    }
}
