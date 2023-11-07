package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.RoomDTO;

import java.util.List;

public interface RoomService {

    RoomDTO createRoom(RoomDTO roomDTO);
    ImgDTO addRoomImage(Long roomId, ImgDTO imgDTO);

    RoomDTO updateRoom(Long rno, RoomDTO roomDTO);
    void deleteRoom(Long rno);
    RoomDTO getRoom(Long rno);
    List<RoomDTO> getAllRooms();
}
