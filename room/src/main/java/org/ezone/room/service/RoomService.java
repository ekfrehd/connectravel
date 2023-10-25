package org.ezone.room.service;

import org.ezone.room.dto.ImgDTO;
import org.ezone.room.dto.ReservationDTO;
import org.ezone.room.dto.RoomDTO;
import org.ezone.room.dto.RoomImgDTO;
import org.springframework.security.core.Authentication;

import java.sql.Date;
import java.util.List;

public interface RoomService {
    public long register(Authentication authentication,RoomDTO dto);

    public List<RoomDTO> getList();

    public RoomDTO get(Long rno);

    public List<RoomImgDTO> getRoomWithImg();

    public List<ImgDTO> getImgList(Long rno);
    public void modify(RoomDTO roomDTO);

    public void remove(Long rno);

    public List<Object[]> get_RvList(Long ano,ReservationDTO dto);

    public List<Object[]> get_salesByDate(Date StartDate, Date EndDate, Long ano);


}

