package com.connectravel.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImgService {

    void addReviewBoardImg(MultipartFile file, Long rbno);

}
