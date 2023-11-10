package com.connectravel.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImgService {

    void addRoomImg(MultipartFile file, Long rno);

    void addReviewBoardImg(MultipartFile file, Long rbno);

    void addAdminBoardImg(MultipartFile file, Long bno);

   // void addTourBoardImg(MultipartFile file, Long tbno);

  //  void addTourBoardReviewImg(MultipartFile file, Long tbrno);

}
