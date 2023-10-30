package org.ezone.room.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ezone.room.entity.Accommodation;
import org.ezone.room.entity.AdminBoard;
import org.ezone.room.entity.ReviewBoard;
import org.ezone.room.entity.Room;
import org.ezone.room.entity.TourBoard;
import org.ezone.room.entity.TourBoardReview;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImgDTO {
    private Long Ino;
    private Room room_id;
    private ReviewBoard reviewBoard;
    private Accommodation accommodationEntity;
    private AdminBoard adminBoard;
    private TourBoard tourBoard;
    private TourBoardReview tourBoardReview;
    private String Imgfile;
}
