package com.connectravel.dto;


import com.connectravel.entity.*;
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
    private Room room;
    private ReviewBoard reviewBoard;
    private Accommodation accommodation;
    private AdminBoard adminBoard;
    private TourBoard tourBoard;
    private TourBoardReview tourBoardReview;
    private String imgFile;
}
