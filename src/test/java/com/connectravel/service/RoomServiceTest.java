package com.connectravel.service;

import com.connectravel.dto.AccommodationDTO;
import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.RoomDTO;
import com.connectravel.entity.*;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.OptionRepository;
import com.connectravel.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoomServiceTest {

    private static final Logger log = LoggerFactory.getLogger(RoomServiceTest.class);

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    private Accommodation savedAccommodation;

    @BeforeEach
    public void setUp() {

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
        savedAccommodation = accommodationRepository.save(accommodation);

    }

    @Test
    @Transactional
    public void testCreateRoom() {
        AccommodationDTO accommodationDTO = new AccommodationDTO();
        accommodationDTO.setAno(savedAccommodation.getAno());

        RoomDTO newRoom = RoomDTO.builder()
                .roomName("Test Room")
                .price(100000)
                .minimumOccupancy(1) // 최소 인원 설정
                .maximumOccupancy(4) // 최대 인원 설정
                .operating(true)
                .accommodationDTO(accommodationDTO)
                .build();

        RoomDTO createdRoom = roomService.createRoom(newRoom);

        assertNotNull(createdRoom, "The room should not be null");
        assertEquals(newRoom.getRoomName(), createdRoom.getRoomName(), "The room names should match");
        assertEquals(newRoom.getMinimumOccupancy(), createdRoom.getMinimumOccupancy(), "The minimum occupancy should match");
        assertEquals(newRoom.getMaximumOccupancy(), createdRoom.getMaximumOccupancy(), "The maximum occupancy should match");

        log.debug("Created Room: {}", createdRoom);
        // 또한, 필드 값이 null이 아닌지도 확인할 수 있습니다.
        assertNotNull(createdRoom.getMinimumOccupancy(), "The minimum occupancy should not be null");
        assertNotNull(createdRoom.getMaximumOccupancy(), "The maximum occupancy should not be null");
    }

    @Test
    @Transactional
    public void testAddRoomImage() {
        // Room 객체 생성 및 저장
        Room room = Room.builder()
                .roomName("Sample Room")
                .price(50000)
                .minimumOccupancy(1)
                .maximumOccupancy(2)
                .operating(true)
                .accommodation(savedAccommodation)
                .build();
        room = roomRepository.save(room); // roomRepository는 주입되어 있어야 합니다.

        // RoomDTO로 변환
        RoomDTO roomDTO = roomService.entityToDTO(room); // entityToDTO는 Room을 RoomDTO로 변환하는 메서드입니다.

        // 이미지 정보 생성
        ImgDTO newImage = ImgDTO.builder()
                .imgFile("room_image.jpg")
                .build();

        // 이미지 추가 로직 실행
        ImgDTO addedImage = roomService.addRoomImage(roomDTO.getRno(), newImage);

        // 검증: 추가된 이미지가 null이 아니어야 하고 파일 이름이 일치해야 합니다.
        assertNotNull(addedImage, "The returned ImgDTO should not be null");
        assertEquals("room_image.jpg", addedImage.getImgFile(), "The image file name should match the expected value");
        log.debug("Added Room Image: {}", addedImage);
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
