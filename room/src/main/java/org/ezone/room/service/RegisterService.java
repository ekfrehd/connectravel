package org.ezone.room.service;

import org.ezone.room.dto.ReservationDTO;
import org.ezone.room.dto.RoomDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface RegisterService{
    Long register(ReservationDTO rvDTO, RoomDTO roomDTO, Authentication authentication);
    List<ReservationDTO> getlist(Authentication authentication);

    boolean cancel(Long rvno,Authentication authentication);
    //
}
