package com.connectravel.dto;


import com.connectravel.entity.ReviewBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImgDTO {

    private Long ino;
    private String imgFile;

    private RoomDTO room;
    private AccommodationDTO accommodation;
    private ReviewBoard reviewBoard;

}
