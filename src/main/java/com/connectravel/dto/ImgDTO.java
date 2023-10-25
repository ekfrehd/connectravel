package com.connectravel.dto;


import com.connectravel.entity.AccommodationEntity;
import com.connectravel.entity.RoomEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImgDTO {
    private Long Ino;
    private RoomEntity room_id;
    private AccommodationEntity accommodationEntity;
    private String Imgfile;

    // private ReviewBoard reviewBoard;
    // private AdminBoard adminBoard;
    // private TourBoard tourBoard;
    // private TourBoardReview tourBoardReview;

}
