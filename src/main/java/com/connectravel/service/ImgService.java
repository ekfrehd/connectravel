package org.ezone.room.service;

import org.ezone.room.dto.ImgDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImgService {

    public void ReviewBoardRegister(MultipartFile file, Long rbno);

}
