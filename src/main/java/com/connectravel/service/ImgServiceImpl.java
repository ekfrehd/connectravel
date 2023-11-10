package com.connectravel.service;

import com.connectravel.entity.*;
import com.connectravel.manager.FileManager;
import com.connectravel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImgServiceImpl implements ImgService {

    private final RoomImgRepository roomImgRepository;
    private final ReviewBoardImgRepository reviewBoardImgRepository;
    private final AdminBoardImgRepository adminBoardImgRepository;
    private final TourBoardImgRepository tourBoardImgRepository;
    private final TourBoardReviewImgRepository tourBoardReviewImgRepository;
    private final FileManager fileManager;


    public void addRoomImg(MultipartFile file, Long rno) {
        String origin = file.getOriginalFilename();
        String realName = fileManager.UUIDMaker(origin);
        boolean is_add =fileManager.add(file,realName);
        if(is_add)
        {
            Room room = Room.builder().rno(rno).build();
            RoomImg roomImg = RoomImg.builder().imgFile(realName).room(room).build();
            roomImgRepository.save(roomImg);
        }
    }

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

    @Override
    public void addAdminBoardImg(MultipartFile file, Long abno) {
        String origin = file.getOriginalFilename();
        String realName = fileManager.UUIDMaker(origin);
        boolean is_add =fileManager.add(file,realName);
        if(is_add)
        {
            AdminBoard adminBoard = AdminBoard.builder().abno(abno).build();
            AdminBoardImg adminBoardImg = AdminBoardImg.builder().imgFile(realName).adminBoard(adminBoard).build();
            adminBoardImgRepository.save(adminBoardImg);
        }
    }

    @Override
    public void addTourBoardImg(MultipartFile file, Long tbno) {
        String origin = file.getOriginalFilename();
        String realName = fileManager.UUIDMaker(origin);
        boolean is_add = fileManager.add(file, realName);
        if (is_add)
        {
            TourBoard tourBoard = TourBoard.builder().tbno(tbno).build();
            TourBoardImg tourBoardImg = TourBoardImg.builder().imgFile(realName).tourBoard(tourBoard).build();
            tourBoardImgRepository.save(tourBoardImg);
        }
    }

    @Override
    public void addTourBoardReviewImg(MultipartFile file, Long tbrno) {
        String origin = file.getOriginalFilename();
        String realName = fileManager.UUIDMaker(origin);
        boolean is_add = fileManager.add(file, realName);
        if (is_add)
        {
            TourBoardReview tourBoardReview = TourBoardReview.builder().tbrno(tbrno).build();
            TourBoardReviewImg tourBoardReviewImg = TourBoardReviewImg.builder().imgFile(realName).tourBoardReview(tourBoardReview).build();
            tourBoardReviewImgRepository.save(tourBoardReviewImg);
        }
    }

}
