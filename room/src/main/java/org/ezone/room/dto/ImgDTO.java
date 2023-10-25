package org.ezone.room.dto;


import org.ezone.room.entity.*;
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
    private ReviewBoard reviewBoard;
    private AccommodationEntity accommodationEntity;
    private AdminBoard adminBoard;
    private TourBoard tourBoard;
    private TourBoardReview tourBoardReview;
    private String Imgfile;
}
