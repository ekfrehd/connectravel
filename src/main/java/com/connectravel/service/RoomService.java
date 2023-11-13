package com.connectravel.service;

import com.connectravel.domain.dto.RoomDTO;
import com.connectravel.domain.entity.Room;

import java.util.List;

public interface RoomService {

    Long registerRoom(RoomDTO roomDTO);

    RoomDTO modifyRoom(Long rno, RoomDTO roomDTO);

    void removeRoom(Long rno);

    RoomDTO getRoom(Long rno);

    List<RoomDTO> listRoomsByAccommodation(Long ano);

    RoomDTO entityToDTO(Room room);

    Room dtoToEntity(RoomDTO roomDTO);

}