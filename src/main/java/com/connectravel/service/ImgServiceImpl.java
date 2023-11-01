package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.entity.*;
import com.connectravel.manager.FileManager;
import com.connectravel.repository.AdminBoardImgRepository;
import com.connectravel.repository.RoomImgRepository;
import com.connectravel.repository.TourBaordImgRepository;
import com.connectravel.repository.TourBoardReivewImgRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImgServiceImpl implements ImgService {

    final RoomImgRepository roomImgRepository;
    //final ReviewBoardImgRepository reviewBoardImgRepository;
    //final AccommodationImgRepository accommodationImgRepository;
    final AdminBoardImgRepository adminBoardImgRepository;
    final TourBaordImgRepository tourBaordImgRepository;
    final TourBoardReivewImgRepository tourBoardReivewImgRepository;


    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FileManager fileManager;


    @Override
    public void remove(Long ino) {
        String filename = roomImgRepository.findById(ino).get().getImgFile(); //select 하고
        //fil name 불러오고 file 삭제하고 db 삭제하고 처리!
        fileManager.remove(filename); //파일삭제
        roomImgRepository.deleteById(ino); //이런식 //db삭제
    }


    @Override
    public ImgDTO get(Long ino) {
        RoomImg img = roomImgRepository.findById(ino).get();
        ImgDTO imgDTO = modelMapper.map(img, ImgDTO.class);
        return imgDTO;
    }

    @Override
    public void update(Long ino, MultipartFile file) {
        String filename = get(ino).getImgFile();
        fileManager.remove(filename); //파일 삭제
        String uuid = filename.split("_")[0]; //전에 사용됬던 이미지 UUID
        String realfilename = uuid + file.getOriginalFilename();
        fileManager.add(file, realfilename); //파일을 생성

        RoomImg sec_entity = roomImgRepository.findById(ino).get();
        RoomImg entity = RoomImg.builder().ino(sec_entity.getIno()).imgFile(realfilename).room(sec_entity.getRoom()).build();
        roomImgRepository.save(entity);
    }

    /*@Override
    public void RoomRegister(MultipartFile file, Long rno) {
        String origin = file.getOriginalFilename(); //사용자가 업로드한 파일 이름 가져오기 xxxx.png xxxx.jpg
        String realname = fileManager.UUIDMaker(origin);// IncrementNumber
        boolean is_add =fileManager.add(file,realname); //파일 생성
        if(is_add) //파일 생성 여부에 따라서 Db에 저장한다.
        {
            //db 저장코드
            RoomEntity roomEntity = RoomEntity.builder().rno(rno).build(); //room entity
            RoomImgEntity entity = RoomImgEntity.builder().Imgfile(realname).room(roomEntity).build(); //이미지 저장
            roomImgRepository.save(entity); //
        }
    }*/


    /*@Override
    public void ReviewBoardRegister(MultipartFile file, Long rbno) {
        String origin = file.getOriginalFilename(); //사용자가 업로드한 파일 이름 가져오기 xxxx.png xxxx.jpg
        String realname = fileManager.UUIDMaker(origin); //UUID 생성
        boolean is_add =fileManager.add(file,realname); //파일 생성
        if(is_add) //파일 생성 여부에 따라서 Db에 저장한다.
        {
            // db 저장코드
            ReviewBoard reviewBoard = ReviewBoard.builder().rbno(rbno).build();
            ReviewBoardImgEntity reviewBoardImgEntity = ReviewBoardImgEntity
                .builder().Imgfile(realname).reviewBoard(reviewBoard).build();
            reviewBoardImgRepository.save(reviewBoardImgEntity);
        }
    }*/

    /*@Override
    public void AccommodationRegister(MultipartFile file, Long ano) {
        String origin = file.getOriginalFilename(); //사용자가 업로드한 파일 이름 가져오기 xxxx.png xxxx.jpg
        String realname = fileManager.UUIDMaker(origin); // IncrementNumber
        boolean is_add =fileManager.add(file,realname); //파일 생성
        if(is_add) //파일 생성 여부에 따라서 Db에 저장한다.
        {
            // db 저장코드
            AccommodationEntity accommodation = AccommodationEntity.builder().ano(ano).build();

            AccommodationImgEntity accommodationImgEntity = AccommodationImgEntity
                    .builder().Imgfile(realname).accommodationEntity(accommodation).build();
            accommodationImgRepository.save(accommodationImgEntity);
        }
    }*/

   /* @Override
    public void AdminBoardRegister(MultipartFile file, Long bno) {
        String origin = file.getOriginalFilename(); //사용자가 업로드한 파일 이름 가져오기 xxxx.png xxxx.jpg
        String realname = fileManager.UUIDMaker(origin); //UUID 생성
        boolean is_add =fileManager.add(file,realname); //파일 생성
        if(is_add) //파일 생성 여부에 따라서 Db에 저장한다.
        {
            // db 저장코드
            AdminBoard adminBoard = AdminBoard.builder().bno(bno).build();
            AdminBoardImg adminBoardImg = AdminBoardImg
                    .builder().imgFile (realname).adminBoard(adminBoard).build();
            adminBoardImgRepository.save(adminBoardImg);
        }
    }*/

    @Override
    public void TourBoardRegister(MultipartFile file, Long tbno) {
        String origin = file.getOriginalFilename(); //사용자가 업로드한 파일 이름 가져오기 xxxx.png xxxx.jpg
        String realname = fileManager.UUIDMaker(origin); //UUID 생성
        boolean is_add = fileManager.add(file, realname); //파일 생성
        if (is_add) //파일 생성 여부에 따라서 Db에 저장한다.
        {
            // db 저장코드
            TourBoard tourBoard = TourBoard.builder().tbno(tbno).build();
            TourBoardImg tourBoardImg = TourBoardImg.builder().imgFile(realname).tourBoard(tourBoard).build();
            tourBaordImgRepository.save(tourBoardImg);
        }
    }

    @Override
    public void TourBoardReviewRegister(MultipartFile file, Long tbrno) {
        String origin = file.getOriginalFilename(); //사용자가 업로드한 파일 이름 가져오기 xxxx.png xxxx.jpg
        String realname = fileManager.UUIDMaker(origin); //UUID 생성
        boolean is_add = fileManager.add(file, realname); //파일 생성
        if (is_add) //파일 생성 여부에 따라서 Db에 저장한다.
        {
            // db 저장코드
            TourBoardReview tourBoardReview = TourBoardReview.builder().tbrno(tbrno).build();
            TourBoardReviewImg tourBoardReviewImg = TourBoardReviewImg.builder().imgFile(realname).tourBoardReview(tourBoardReview).build();
            tourBoardReivewImgRepository.save(tourBoardReviewImg);
        }
    }
}
