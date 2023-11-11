package com.connectravel.service.impl;

import com.connectravel.domain.entity.*;
import com.connectravel.manager.FileManager;
import com.connectravel.repository.*;
import com.connectravel.service.ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.BiConsumer;

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
        addImage(file, rno, (realName, id) -> {
            Room room = Room.builder().rno(id).build();
            RoomImg roomImg = RoomImg.builder().imgFile(realName).room(room).build();
            roomImgRepository.save(roomImg);
        });
    }

    @Override
    public void addReviewBoardImg(MultipartFile file, Long rbno) {
        addImage(file, rbno, (realName, id) -> {
            ReviewBoard reviewBoard = ReviewBoard.builder().rbno(rbno).build();
            ReviewBoardImg reviewBoardImgEntity = ReviewBoardImg
                    .builder().imgFile(realName).reviewBoard(reviewBoard).build();
            reviewBoardImgRepository.save(reviewBoardImgEntity);
        });
    }

    @Override
    public void addAdminBoardImg(MultipartFile file, Long abno) {
        addImage(file, abno, (realName, id) -> {
            AdminBoard adminBoard = AdminBoard.builder().abno(abno).build();
            AdminBoardImg adminBoardImg = AdminBoardImg.builder().imgFile(realName).adminBoard(adminBoard).build();
            adminBoardImgRepository.save(adminBoardImg);
        });
    }

    @Override
    public void addTourBoardImg(MultipartFile file, Long tbno) {
        addImage(file, tbno, (realName, id) -> {
            TourBoard tourBoard = TourBoard.builder().tbno(tbno).build();
            TourBoardImg tourBoardImg = TourBoardImg.builder().imgFile(realName).tourBoard(tourBoard).build();
            tourBoardImgRepository.save(tourBoardImg);
        });
    }

    @Override
    public void addTourBoardReviewImg(MultipartFile file, Long tbrno) {
        addImage(file, tbrno, (realName, id) -> {
            TourBoardReview tourBoardReview = TourBoardReview.builder().tbrno(tbrno).build();
            TourBoardReviewImg tourBoardReviewImg = TourBoardReviewImg.builder().imgFile(realName).tourBoardReview(tourBoardReview).build();
            tourBoardReviewImgRepository.save(tourBoardReviewImg);
        });
    }

    /* 범용 메서드 */
    private void addImage(MultipartFile file, Long entityId, BiConsumer<String, Long> saveImageConsumer) {
        String origin = file.getOriginalFilename();
        String realName = fileManager.UUIDMaker(origin);
        boolean isAdd = fileManager.add(file, realName);
        if (isAdd) {
            saveImageConsumer.accept(realName, entityId);
        }
    }

}