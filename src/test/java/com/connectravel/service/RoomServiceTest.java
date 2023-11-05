package com.connectravel.service;

import com.connectravel.dto.RoomDTO;
import com.connectravel.entity.*;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.OptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoomServiceTest {

    private static final Logger log = LoggerFactory.getLogger(RoomServiceTest.class);

    @Autowired
    private RoomService roomService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @BeforeEach
    public void registerAccommodation() {

        Member member = Member.builder()
                .name("roomName")
                .nickName("roomNickName")
                .email("roomEmail")
                .build();
        memberRepository.save(member);

        // 옵션 2개 추가
        Option option1 = Option.builder()
                .optionName("침대")
                .build();

        Option option2 = Option.builder()
                .optionName("온돌")
                .build();

        optionRepository.save(option1);
        optionRepository.save(option2);

        // Accommodation 정보 생성
        Accommodation accommodation = Accommodation.builder()
                .accommodationName("테스트 숙소")
                .sellerName(member.getName())
                .postal(12345)
                .address("제주도에 있어요")
                .count(0)
                .region("제주")
                .tel("123-456-7890")
                .accommodationType("게스트하우스")
                .member(member)
                .build();

        // 이미지 2개 추가
        AccommodationImg img1 = new AccommodationImg();
        img1.setImgFile("image1.jpg");
        accommodation.addImage(img1);

        AccommodationImg img2 = new AccommodationImg();
        img2.setImgFile("image2.jpg");
        accommodation.addImage(img2);

        // 옵션과 숙박업소의 연관 관계 매핑
        AccommodationOption accommodationOption1 = new AccommodationOption();
        accommodationOption1.setOption(option1);
        accommodation.addAccommodationOption(accommodationOption1);

        AccommodationOption accommodationOption2 = new AccommodationOption();
        accommodationOption2.setOption(option2);
        accommodation.addAccommodationOption(accommodationOption2);

        // 저장
        accommodationRepository.save(accommodation);

    }

    @Test
    @Transactional
    public void testCreateRoom() {
        RoomDTO newRoom = RoomDTO.builder()
                .roomName("Test Room")
                .price(100000)
                .operating(true)
                .build();

        RoomDTO createdRoom = roomService.createRoom(newRoom);

        log.debug("Created Room : {} ", createdRoom);
        assertNotNull(createdRoom);
        assertEquals(newRoom.getRoomName(), createdRoom.getRoomName());


    }

   /* @Test
    public void testUpdateRoom() {
        // 업데이트를 원하는 room의 ID
        Long roomId = 1L;
        RoomDTO roomToUpdate = roomService.getRoom(roomId);
        roomToUpdate.setPrice(120000);

        RoomDTO updatedRoom = roomService.updateRoom(roomId, roomToUpdate);
        assertNotNull(updatedRoom);
        assertEquals(120000, updatedRoom.getPrice());
    }

    @Test
    public void testGetRoom() {
        Long roomId = 1L;
        RoomDTO room = roomService.getRoom(roomId);
        assertNotNull(room);
        assertEquals(roomId, room.getRno());
    }

    @Test
    public void testGetAllRooms() {
        List<RoomDTO> rooms = roomService.getAllRooms();
        assertNotNull(rooms);
        assertFalse(rooms.isEmpty());
    }

    @Test
    public void testDeleteRoom() {
        Long roomId = 1L;
        roomService.deleteRoom(roomId);
        assertThrows(EntityNotFoundException.class, () -> roomService.getRoom(roomId));
    }*/
}
