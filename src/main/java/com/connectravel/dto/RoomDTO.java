package com.connectravel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long rno;  //방번호
    private String roomName; //방 이름
    private int price; //방 갸격
    private boolean operating;
    private String content;

    private AccommodationDTO accommodationDTO;

}
