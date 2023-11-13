package com.connectravel.repository.search;

import com.connectravel.entity.*;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.OptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SearchRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(SearchRepositoryTest.class);

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        Member member = Member.builder()
                .name("TestName")
                .nickName("TestNickName")
                .email("TestEmail")
                .build();
        memberRepository.save(member);

        // 다양한 옵션 추가
        Option option1 = Option.builder().optionName("wifi").build();
        Option option2 = Option.builder().optionName("세탁기").build();
        // 추가 옵션들...
        optionRepository.save(option1);
        optionRepository.save(option2);
        // 추가 옵션 저장...

        // Accommodation 정보 생성
        Accommodation accommodation = Accommodation.builder()
                .accommodationName("Test Accommodation")
                .postal(12345)
                .sellerName(member.getName())
                .sellerEmail(member.getEmail())
                .address("123 Test Street, Test City")
                .count(0)
                .region("Seoul")
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

        // 옵션과 숙박업소 연관 관계 설정
        AccommodationOption accommodationOption1 = new AccommodationOption();
        accommodationOption1.setOption(option1);
        accommodation.addAccommodationOption(accommodationOption1);

        AccommodationOption accommodationOption2 = new AccommodationOption();
        accommodationOption2.setOption(option2);
        accommodation.addAccommodationOption(accommodationOption2);
        // 추가 옵션 연관 관계 설정...

        accommodationRepository.save(accommodation);

        // 다양한 방 추가
        Room room1 = Room.builder()
                .roomName("테스트 방 1")
                .price(10000)
                .minimumOccupancy(1)
                .maximumOccupancy(2)
                .operating(true)
                .accommodation(accommodation)
                .build();

        Room room2 = Room.builder()
                .roomName("테스트 방 2")
                .price(15000)
                .minimumOccupancy(2)
                .maximumOccupancy(3)
                .operating(true)
                .accommodation(accommodation)
                .build();
        // 추가 방 생성...

        accommodation.addRoom(room1);
        accommodation.addRoom(room2);
        // 추가 방을 숙박업소에 연결...

        accommodationRepository.save(accommodation);
    }


    @Test
    public void testSearchByLocation() {
        List<Accommodation> results = accommodationRepository.findByRegion("Seoul");
        assertFalse(results.isEmpty());

        for(Accommodation accommodation : results) {
            log.debug("Accommodation Name: {}", accommodation.getAccommodationName());
            log.debug("Address: {}", accommodation.getAddress());
            log.debug("Tel: {}", accommodation.getTel());
            log.debug("Content: {}", accommodation.getContent());
            log.debug("Region: {}", accommodation.getRegion());

            // 옵션 정보 출력
            log.debug("Options:");
            for(AccommodationOption option : accommodation.getAccommodationOptions()) {
                log.debug(" - Option Name: {}", option.getOption());
            }

            // 이미지 정보 출력
            log.debug("Images:");
            for(AccommodationImg img : accommodation.getImages()) {
                log.debug(" - Image : {}", img.getImgFile());
            }

            // 방 정보 출력
            log.debug("Rooms:");
            for(Room room : accommodation.getRooms()) {
                log.debug(" - Room Name: {}", room.getRoomName());
                log.debug(" - Price: {}", room.getPrice());
                log.debug(" - Operating: {}", room.isOperating());
                log.debug(" - Content: {}", room.getContent());

                // 방의 이미지 정보 출력
                log.debug("   Room Images:");
                for(RoomImg roomImg : room.getImages()) {
                    log.debug("     - Image : {}", roomImg.getImgFile());
                }

                // 방의 예약 정보 출력 (필요하다면)
                /*log.debug("   Reservations:");
                for(Reservation reservation : room.getReservations()) {
                    log.debug("     - Reservation Date: {}", reservation.getDate());
                }*/
            }

            log.debug("--------------------");
        }

        results = accommodationRepository.findByRegion("InvalidLocation");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchByOptions() {
        // 옵션 ID 집합 생성
        Set<Long> optionIds = Set.of(1L, 2L); // 예시 옵션 ID

        // 옵션 기반 검색 수행
        List<Accommodation> results = accommodationRepository.findByOptions(optionIds);
        assertFalse(results.isEmpty());

        // 결과 확인
        for (Accommodation accommodation : results) {
            log.debug("Accommodation Name: {}", accommodation.getAccommodationName());
            // 추가적인 결과 출력
        }
    }

    @Test
    public void testSearchByRoomCriteria() {
        // 방 검색 조건 설정
        int price = 20000; // 예시 가격
        int minimumOccupancy = 1; // 최소 인원
        int maximumOccupancy = 2; // 최대 인원
        boolean operating = true; // 운영 여부

        // 방 기반 검색 수행
        List<Accommodation> results = accommodationRepository.findByRoomCriteria(price, minimumOccupancy, maximumOccupancy, operating);
        assertFalse(results.isEmpty());

        // 결과 확인
        for (Accommodation accommodation : results) {
            log.debug("Accommodation Name: {}", accommodation.getAccommodationName());
            // 추가적인 결과 출력
        }
    }


}
