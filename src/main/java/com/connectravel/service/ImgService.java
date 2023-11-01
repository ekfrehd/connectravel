package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImgService {
    void remove(Long ino);

    ImgDTO get(Long ino);

    void update(Long ino, MultipartFile file);
/*
    public void RoomRegister(MultipartFile file, Long rno);

    public void ReviewBoardRegister(MultipartFile file, Long rbno);

    public void AccommodationRegister(MultipartFile file, Long ano);*/

    //public void AdminBoardRegister(MultipartFile file, Long bno);

    void TourBoardRegister(MultipartFile file, Long tbno);

    void TourBoardReviewRegister(MultipartFile file, Long tbrno);
}
