package org.ezone.room.service;

import org.ezone.room.security.CustomUserDetails;
import org.ezone.room.dto.*;
import org.ezone.room.entity.AccommodationEntity;
import org.ezone.room.entity.Member;
import org.ezone.room.entity.RoomEntity;
import org.ezone.room.repository.AccommodationRepository;
import org.ezone.room.repository.ReviewBoardRepository;
import org.ezone.room.repository.RoomImgRepository;
import org.ezone.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service //빈등록
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{
    final RoomRepository repository;
    final RoomImgRepository imgRepository;
    final AccommodationRepository AccRepository;
    final ReviewBoardRepository reviewBoardRepository;

    @Autowired
    ModelMapper modelMapper;

    //room insert
    //방을 register 할때는 member로 숙소주인을 찾아내고 insert한다 라는 조건이 추가됨.
    @Override
    public long register(Authentication authentication,RoomDTO dto) {
        AccommodationEntity Accentity =AccRepository.findByEmail(authentication.getName()); //주인정보를 통한 숙소 검색
        AccommodationDTO Accdto = modelMapper.map(Accentity, AccommodationDTO.class); //entity to dto 규약
        dto.setAcc_id(Accdto); //set acc
        RoomEntity entity = modelMapper.map(dto, RoomEntity.class); // dto to entity
        return repository.save(entity).getRno(); //save
    }

    //room read (list)
    @Override
    public List<RoomDTO> getList() {
        List<RoomDTO> list = new ArrayList<>();
        repository.findAll().forEach(i -> {
            RoomDTO dto = modelMapper.map(i,RoomDTO.class);
            list.add(dto);
        });
        return list;
    }

    @Override
    public RoomDTO get(Long rno) {
        RoomEntity entity = repository.findById(rno).get();
        RoomDTO roomDTO = modelMapper.map(entity,RoomDTO.class);
        return roomDTO;
    }

    //get dto

    //조인을 통해소 object로 된 배열을 다시 쪼갬 ( room,img)그리고 다시 합침
    @Override
    public List<RoomImgDTO> getRoomWithImg() {
        List<RoomImgDTO> roomImgDTOS = new ArrayList<>(); //리턴할 ROOM+IMG DTO

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        AccommodationEntity accommodation = AccRepository.findByEmail(member.getEmail());

        repository.findRoomWithImages(accommodation.getAno()).forEach(i -> {
            RoomDTO roomDTO = modelMapper.map(i[0], RoomDTO.class);
            ImgDTO imgDTO = (i[1] != null) ? modelMapper.map(i[1], ImgDTO.class) : new ImgDTO();
            RoomImgDTO roomImgDTO = RoomImgDTO.builder().
                    room(roomDTO).
                    img(imgDTO).
                    build();
            roomImgDTOS.add(roomImgDTO);
        });
        return roomImgDTOS;
    }

    //이미지 리스트를 불러오는 메서드
    @Override
    public List<ImgDTO> getImgList(Long rno) {
        List<ImgDTO> list = new ArrayList<>();
        RoomEntity entity = repository.findById(rno).get();
        imgRepository.GetImgbyRoomId(entity).forEach(i -> { //이미지는 룸을 참조하고 있다 그러니 이미지가 참조하는 룸에 해당하는 모든 이미지를 불러온다
            ImgDTO imgDTO = modelMapper.map(i,ImgDTO.class); //dto변환
            list.add(imgDTO); // list화
        });
        return list;
    }


    //방수정
    @Override
    public void modify(RoomDTO roomDTO) { //사용자가 입력한 dto를
        RoomEntity roomEntity = repository.findByRoom_nameByRno(roomDTO.getRno());
        roomEntity.setRoom_name(roomDTO.getRoom_name());
        roomEntity.setContent(roomDTO.getContent());
        roomEntity.setPrice(roomDTO.getPrice());
        roomEntity.setOperating(roomDTO.isOperating());
        repository.save(roomEntity); //db에 수정한다. : save 메서드는 db에 데이터가 없으면 insert 있으면 update 한다.
    }

    @Transactional
    @Override
    public void remove(Long rno) {
        reviewBoardRepository.deleteByRoom_Rno(rno);
        repository.deleteById(rno);
    }

    //방 리스트(예약,이미지 포함)
    @Override
    public List<Object[]> get_RvList(Long ano,ReservationDTO dto) {
        List<Object[]> list = repository.findByDateandImgandAno(dto.getStartDate(), dto.getEndDate()
                ,ano);
        List<Object[]> newlist = new ArrayList<>();
        list.forEach(i -> {
            Object[] objects = new Object[3];
            objects[0] = modelMapper.map(i[0],RoomDTO.class); //Entity to Dto
            objects[1] = i[1]; //img
            objects[2] = i[2]; //rv
            newlist.add(objects);
        });
        return newlist;
    }

    public List<Object[]> get_salesByDate(Date StartDate, Date EndDate, Long ano) {
        List<Object[]> list = repository.findSalesByDate(StartDate,EndDate,ano);
        return list;
    }

}
