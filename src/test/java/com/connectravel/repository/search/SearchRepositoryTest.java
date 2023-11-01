package com.connectravel.repository.search;

import com.connectravel.entity.*;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.OptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SearchRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(SearchRepositoryTest.class);

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @BeforeEach
    public void setUp() {
        Member member = Member.builder()
                .name("TestName")
                .nickName("TestNickName")
                .email("TestEmail")
                .build();

        // 옵션 2개 추가
        Option option1 = Option.builder()
                .optionCategory("공용")
                .optionName("wifi")
                .build();

        Option option2 = Option.builder()
                .optionCategory("공용")
                .optionName("세탁기")
                .build();

        optionRepository.save(option1);
        optionRepository.save(option2);

        // Accommodation 정보 생성
        Accommodation accommodation = Accommodation.builder()
                .name("Test Accommodation")
                .postal(12345)
                .adminName(member.getName())
                .email(member.getEmail())
                .address("123 Test Street, Test City")
                .count(0)
                .tel("123-456-7890")
                .accommodationType("Hotel")
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

        // Room 정보 생성
        Room room = Room.builder()
                .roomName("테스트 방")
                .price(10000)
                .operating(true)
                .content("테스트 방 내용")
                .accommodation(accommodation)
                .build();

        // RoomImg 추가
        RoomImg roomImg = new RoomImg();
        roomImg.setImgFile("image.jpg");
        room.addImage(roomImg);

        // Room 저장 (RoomImg는 Room에 종속되므로 별도의 저장은 필요 없습니다)
        accommodation.getRooms().add(room);
        // 저장된 Accommodation 엔터티의 상태를 갱신
        accommodationRepository.save(accommodation);
    }


    @Test
    public void testSearchByLocation() {
        List<Accommodation> results = searchRepository.findByLocation("Seoul");
        assertFalse(results.isEmpty());

        results = searchRepository.findByLocation("InvalidLocation");
        assertTrue(results.isEmpty());
    }

    /*@Test
    public void testSearchByOption() {
        List<Accommodation> results = searchRepository.findByOption("WiFi");
        assertFalse(results.isEmpty());

        results = searchRepository.findByOption("NonExistentOption");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchByPriceRange() {
        List<Accommodation> results = searchRepository.findByPriceRange(10000, 50000);
        assertFalse(results.isEmpty());

        results = searchRepository.findByPriceRange(900000, 950000);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchByCapacityAndDates() {
        List<Accommodation> results = searchRepository.findByCapacityAndDates(3, "2023-12-01", "2023-12-07");
        assertFalse(results.isEmpty());

        results = searchRepository.findByCapacityAndDates(10, "2023-12-01", "2023-12-07");
        assertTrue(results.isEmpty());
    }*/

    // Add more test methods as required
}
