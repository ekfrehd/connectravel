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
import java.util.List;

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

        savedAccommodation = accommodationRepository.save(accommodation);

        // 저장된 Accommodation에 Room 객체 생성 및 저장
        createAndSaveRoom(savedAccommodation, "Standard Room", 100000, 1, 2, true);
        createAndSaveRoom(savedAccommodation, "Deluxe Room", 200000, 2, 4, true);
    }

    // Room 객체를 생성하고 저장하는 공통 메서드
    private RoomDTO createAndSaveRoom(Accommodation accommodation, String roomName, int price, int minOccupancy, int maxOccupancy, boolean operating) {
        RoomDTO newRoom = RoomDTO.builder()
                .roomName(roomName)
                .price(price)
                .minimumOccupancy(minOccupancy)
                .maximumOccupancy(maxOccupancy)
                .operating(operating)
                .accommodationDTO(AccommodationDTO.builder().ano(accommodation.getAno()).build())
                .build();

        return roomService.createRoom(newRoom);
    }

    @Test
    @Transactional
    public void testCreateRoom() {
        // AccommodationDTO 설정
        AccommodationDTO accommodationDTO = new AccommodationDTO();
        accommodationDTO.setAno(savedAccommodation.getAno());

        // RoomDTO 생성
        RoomDTO newRoom = RoomDTO.builder()
                .roomName("Test Room")
                .price(100000)
                .minimumOccupancy(1)
                .maximumOccupancy(4)
                .operating(true)
                .accommodationDTO(accommodationDTO)
                .build();

        // createRoom 메서드를 통해 신규 방 생성
        RoomDTO createdRoom = roomService.createRoom(newRoom);

        // 생성된 방의 정보 검증
        assertNotNull(createdRoom, "The room should not be null");
        assertEquals(newRoom.getRoomName(), createdRoom.getRoomName(), "The room names should match");

        // 로깅
        log.debug("Created Room: {}, Accommodation ANO: {}", createdRoom, accommodationDTO.getAno());
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
        room = roomRepository.save(room);

        // RoomDTO로 변환하지 않고 서비스에서 제공하는 메서드로 RoomDTO를 얻음
        RoomDTO roomDTO = roomService.getRoom(room.getRno());

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

    @Test
    @Transactional
    public void testUpdateRoom() {
        // 업데이트를 원하는 room의 ID
        Long roomId = savedAccommodation.getRooms().get(0).getRno(); // 예를 들어 첫번째 방 ID를 가져옴
        RoomDTO roomToUpdate = roomService.getRoom(roomId); // 기존 방 정보를 가져옴
        roomToUpdate.setPrice(120000); // 가격 변경

        // 업데이트 메서드 호출
        RoomDTO updatedRoom = roomService.updateRoom(roomId, roomToUpdate);

        // 검증
        assertNotNull(updatedRoom);
        assertEquals(120000, updatedRoom.getPrice());

        log.debug("Updated Room: {}", updatedRoom);
    }

    @Test
    @Transactional
    public void testGetRoom() {
        // 이미 저장된 방 ID를 사용, 예를 들어 첫번째 방 ID를 가져옴
        Long roomId = savedAccommodation.getRooms().get(0).getRno();

        // 방 조회 메서드 호출
        RoomDTO room = roomService.getRoom(roomId);

        // 검증
        assertNotNull(room);
        assertEquals(roomId, room.getRno());

        log.debug("Get room : {}", room);
    }

    @Test
    @Transactional
    public void testGetAllRooms() {
        // 모든 방 조회 메서드 호출
        List<RoomDTO> rooms = roomService.getAllRooms();

        // 검증
        assertNotNull(rooms);
        assertFalse(rooms.isEmpty());

        log.debug("Get room : {}", rooms);
    }

   /* @Test
    @Transactional
    public void testDeleteRoom() {
        // 삭제를 원하는 방 ID를 설정
        Long roomId = savedAccommodation.getRooms().get(0).getRno(); // 예를 들어 첫번째 방 ID를 가져옴

        // 삭제 메서드 호출
        roomService.deleteRoom(roomId);

        // 방이 삭제되었는지 확인하기 위한 검증
        assertThrows(EntityNotFoundException.class, () -> roomService.getRoom(roomId));
    }*/

}
