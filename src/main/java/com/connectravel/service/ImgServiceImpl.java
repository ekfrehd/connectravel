package org.ezone.room.service;

import lombok.RequiredArgsConstructor;
import org.ezone.room.dto.ImgDTO;
import org.ezone.room.entity.AdminBoard;
import org.ezone.room.entity.ReviewBoard;
import org.ezone.room.entity.ReviewBoardImg;
import org.ezone.room.entity.RoomImg;
import org.ezone.room.entity.TourBoard;
import org.ezone.room.entity.TourBoardImg;
import org.ezone.room.entity.TourBoardReview;
import org.ezone.room.entity.TourBoardReviewImg;
import org.ezone.room.manager.FileManager;
import org.ezone.room.repository.ReviewBoardImgRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImgServiceImpl implements ImgService{

    final ReviewBoardImgRepository reviewBoardImgRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FileManager fileManager;

    @Override
    public void ReviewBoardRegister(MultipartFile file, Long rbno) {
        String origin = file.getOriginalFilename(); //사용자가 업로드한 파일 이름 가져오기 xxxx.png xxxx.jpg
        String realname = fileManager.UUIDMaker(origin); //UUID 생성
        boolean is_add =fileManager.add(file,realname); //파일 생성
        if(is_add) //파일 생성 여부에 따라서 Db에 저장한다.
        {
            // db 저장코드
            ReviewBoard reviewBoard = ReviewBoard.builder().rbno(rbno).build();
            ReviewBoardImg reviewBoardImgEntity = ReviewBoardImg
                .builder().imgFile(realname).reviewBoard(reviewBoard).build();
            reviewBoardImgRepository.save(reviewBoardImgEntity);
        }
    }

}
