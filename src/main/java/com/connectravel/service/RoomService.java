package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.RoomDTO;

import java.util.List;

public interface RoomService {

    RoomDTO registerRoom(RoomDTO roomDTO);
    ImgDTO addRoomImage(Long roomId, ImgDTO imgDTO);
    RoomDTO modifyRoom(Long rno, RoomDTO roomDTO);
    void removeRoom(Long rno);
    RoomDTO getRoom(Long rno);
    List<RoomDTO> listAllRooms();
}
