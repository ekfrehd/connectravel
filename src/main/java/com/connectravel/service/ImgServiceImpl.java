package com.connectravel.service;

import com.connectravel.entity.ReviewBoard;
import com.connectravel.entity.ReviewBoardImg;
import com.connectravel.manager.FileManager;
import com.connectravel.repository.ReviewBoardImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImgServiceImpl implements ImgService {

    private final ReviewBoardImgRepository reviewBoardImgRepository;
    private final FileManager fileManager;

    @Override
    public void addReviewBoardImg(MultipartFile file, Long rbno) {
        String origin = file.getOriginalFilename(); //사용자가 업로드한 파일 이름 가져오기 xxxx.png xxxx.jpg
        String realName = fileManager.UUIDMaker(origin); //UUID 생성
        boolean is_add =fileManager.add(file,realName); //파일 생성
        if(is_add) //파일 생성 여부에 따라서 Db에 저장한다.
        {
            ReviewBoard reviewBoard = ReviewBoard.builder().rbno(rbno).build();
            ReviewBoardImg reviewBoardImgEntity = ReviewBoardImg
                .builder().imgFile(realName).reviewBoard(reviewBoard).build();
            reviewBoardImgRepository.save(reviewBoardImgEntity);
        }
    }

}
