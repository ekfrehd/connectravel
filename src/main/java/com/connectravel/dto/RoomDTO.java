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
    private Long rno;
    private String roomName;
    private int price;
    private boolean operating;
    private String content;

    private AccommodationDTO accommodationDTO;

}
