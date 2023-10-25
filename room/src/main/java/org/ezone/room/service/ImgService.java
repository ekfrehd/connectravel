package org.ezone.room.service;

import org.ezone.room.dto.ImgDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImgService {
    public void remove(Long ino);

    public ImgDTO get(Long ino);

    public void update(Long ino, MultipartFile file);

    public void RoomRegister(MultipartFile file, Long rno);

    public void ReviewBoardRegister(MultipartFile file, Long rbno);

    public void AccommodationRegister(MultipartFile file, Long ano);

    public void AdminBoardRegister(MultipartFile file, Long bno);

    public void TourBoardRegister(MultipartFile file, Long tbno);

    public void TourBoardReviewRegister(MultipartFile file, Long tbrno);
}
