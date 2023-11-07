package com.connectravel.repository;

import com.connectravel.entity.AccommodationOption;
import com.connectravel.entity.Room;
import com.connectravel.entity.RoomImg;
import com.connectravel.entity.Accommodation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class RoomRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(RoomRepositoryTest.class);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        Accommodation accommodation = Accommodation.builder()
                .accommodationName("테스트 숙소")
                .postal(12345)
                .sellerName("운영자")
                .address("테스트 주소")
                .count(0)
                .accommodationType("테스트 타입")
                .region("테스트 지역")
                .tel("010-1234-5678")
                .content("테스트 내용")
                .email("test@test.com")
                .intro("테스트 소개")
                .build();

        entityManager.persist(accommodation);

        Room room = Room.builder()
                .roomName("테스트 방")
                .price(10000)
                .minimumOccupancy(1) // 최소 인원 설정
                .maximumOccupancy(2) // 최대 인원 설정
                .operating(true)
                .content("테스트 방 내용")
                .accommodation(accommodation)
                .build();

        RoomImg roomImg = new RoomImg();
        roomImg.setImgFile("image.jpg");
        room.addImage(roomImg);

        entityManager.persist(roomImg);
        entityManager.persist(room);
    }

    @Test
    void saveRoom() {
        Room newRoom = Room.builder()
                .roomName("새 테스트 방")
                .price(20000)
                .minimumOccupancy(1)
                .maximumOccupancy(4)
                .operating(true)
                .content("새 테스트 방 내용")
                .build();

        Room savedRoom = roomRepository.save(newRoom);

        log.debug("Saved Room: {}", savedRoom);

        assertThat(savedRoom).isNotNull();
        assertThat(savedRoom.getRoomName()).isEqualTo("새 테스트 방");
    }

    @Test
    void findRoomByName() {
        Room foundRoom = roomRepository.findByRoomName("테스트 방").orElse(null);

        log.debug("Found Room: {}", foundRoom);

        assertThat(foundRoom.getRoomName()).isEqualTo("테스트 방");
    }

    @Test
    void updateRoom() {
        Room existingRoom = roomRepository.findByRoomName("테스트 방").orElse(null);
        existingRoom.setRoomName("수정된 테스트 방");

        Room updatedRoom = roomRepository.save(existingRoom);

        log.debug("Updated Room: {}", updatedRoom);

        assertThat(updatedRoom.getRoomName()).isEqualTo("수정된 테스트 방");
    }

    @Test
    void deleteRoom() {
        Room existingRoom = roomRepository.findByRoomName("테스트 방").orElse(null);
        roomRepository.delete(existingRoom);

        Room deletedRoom = roomRepository.findByRoomName("테스트 방").orElse(null);

        log.debug("Deleted Room: {}", deletedRoom);

        assertThat(deletedRoom).isNull();
    }

    @Test
    void testRoomWithImages() {
        Room room = roomRepository.findByRoomName("테스트 방").orElse(null);
        RoomImg roomImg = new RoomImg();
        roomImg.setImgFile("image.jpg");
        room.addImage(roomImg);

        entityManager.persist(roomImg);

        Room roomWithImg = roomRepository.findByRoomName("테스트 방").orElse(null);

        log.debug("Room Images: {}", roomWithImg.getImages());

        assertThat(roomWithImg.getImages()).isNotEmpty();
    }

    @Test
    void testRoomWithRemoveImages() {
        // 1. "테스트 방"을 불러옵니다.
        Room room = roomRepository.findByRoomName("테스트 방").orElse(null);
        assertNotNull(room, "Room must exist to perform remove image test.");

        // 2. 첫 번째 이미지를 삭제합니다.
        if (!room.getImages().isEmpty()) {
            RoomImg roomImg = room.getImages().get(0);
            room.removeImage(roomImg);

            // 데이터베이스에서 이미지 삭제
            entityManager.remove(roomImg);
            entityManager.flush();  // DB와 동기화
        }

        // 3. 변경사항을 저장합니다.
        roomRepository.save(room);

        // 4. 이미지가 제대로 삭제되었는지 확인합니다.
        Room updatedRoom = roomRepository.findByRoomName("테스트 방").orElse(null);
        log.debug("Updated Room Images Size: {}", updatedRoom.getImages().size());
        assertEquals(0, updatedRoom.getImages().size());
    }



}
