package com.connectravel.dto;


import com.connectravel.entity.Accommodation;
import com.connectravel.entity.Room;
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
    private Long ino;
    private Room room;
    private Accommodation accommodation;
    private String imgFile;
}
