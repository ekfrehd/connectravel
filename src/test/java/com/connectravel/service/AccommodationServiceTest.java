package com.connectravel.service;

import com.connectravel.dto.AccommodationDTO;
import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.OptionDTO;
import com.connectravel.entity.*;
import com.connectravel.repository.AccommodationRepository;
import com.connectravel.repository.OptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccommodationServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AccommodationServiceTest.class);

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        Member member = Member.builder()
                .name("TestName")
                .nickName("TestNickName")
                .email("TestEmail")
                .build();

        // Accommodation 초기 정보 생성
        Accommodation accommodation = Accommodation.builder()
                .name("Test Service Accommodation")
                .postal(54321)
                .adminName(member.getName())
                .email(member.getEmail())
                .address("789 Test Service Ave, Service City")
                .count(0)
                .tel("987-654-3210")
                .accommodationType("Service Hotel")
                .member(member)
                .build();

        // 이미지 2개 추가
        AccommodationImg img1 = new AccommodationImg();
        img1.setImgFile("service_image1.jpg");
        accommodation.addImage(img1);

        AccommodationImg img2 = new AccommodationImg();
        img2.setImgFile("service_image2.jpg");
        accommodation.addImage(img2);

        // 옵션 2개 추가
        Option option1 = Option.builder()
                .optionCategory("서비스")
                .optionName("TV")
                .build();

        Option option2 = Option.builder()
                .optionCategory("서비스")
                .optionName("에어컨")
                .build();

        optionRepository.save(option1);
        optionRepository.save(option2);

        AccommodationOption accommodationOption1 = new AccommodationOption();
        accommodationOption1.setAccommodation(accommodation);
        accommodationOption1.setOption(option1);
        accommodation.addAccommodationOption(accommodationOption1);

        AccommodationOption accommodationOption2 = new AccommodationOption();
        accommodationOption2.setAccommodation(accommodation);
        accommodationOption2.setOption(option2);
        accommodation.addAccommodationOption(accommodationOption2);

        // 저장
        accommodationRepository.save(accommodation);
    }

    @Test // 숙소의 상세 정보 수정
    @Transactional
    void modifyAccommodationDetails() {
        // 1. 테스트 데이터 설정
        AccommodationDTO dtoToUpdate = new AccommodationDTO();
        dtoToUpdate.setAno(1L); // 수정하려는 숙소의 ID
        dtoToUpdate.setName("Updated Accommodation Name"); // 수정하려는 이름
        dtoToUpdate.setTel("987-654-3210"); // 수정하려는 전화번호

        // 2. AccommodationService의 modifyAccommodationDetails 메서드 호출
        AccommodationDTO updatedDto = accommodationService.modifyAccommodationDetails(dtoToUpdate);

        // 3. 반환된 DTO를 검증
        assertNotNull(updatedDto);
        assertEquals("Updated Accommodation Name", updatedDto.getName());
        assertEquals("987-654-3210", updatedDto.getTel());

        log.info("Updated Accommodation: " + updatedDto.getName() + ", Tel: " + updatedDto.getTel());
    }

    @Test
    @Transactional
    void getAccommodationDetails() {
        AccommodationDTO foundDto = accommodationService.getAccommodationDetails(1L);

        assertNotNull(foundDto);
        log.info("Accommodation Details:");
        log.info("Name: " + foundDto.getName());
        log.info("Tel: " + foundDto.getTel());
        log.info("Address: " + foundDto.getAddress());
        log.info("Admin Name: " + foundDto.getAdminName());
        log.info("Email: " + foundDto.getEmail());
        log.info("Type: " + foundDto.getAccommodationType());
        log.info("Region: " + foundDto.getRegion());
        log.info("Intro: " + foundDto.getIntro());
        log.info("Content: " + foundDto.getContent());
        log.info("Postal: " + foundDto.getPostal());
        log.info("Min Price: " + foundDto.getMinPrice());

        // 이미지 정보 출력
        log.info("Images:");
        for (String imgFile : foundDto.getImgFiles()) {  // 변경된 부분
            log.info("- Image File: " + imgFile);
        }

        // 옵션 정보 출력
        log.info("Options:");
        for (OptionDTO option : foundDto.getOptionDTO()) {
            log.info("- Option Category: " + option.getOptionCategory() + ", Option Name: " + option.getOptionName());
        }
    }



   /* @Test
    void removeAccommodation() {
        boolean isRemoved = accommodationService.removeAccommodation(1L);

        assertTrue(isRemoved);
    }



    @Test
    void listAllAccommodations() {
        List<AccommodationDTO> accommodations = accommodationService.listAllAccommodations();

        assertFalse(accommodations.isEmpty());
    }*/
}
