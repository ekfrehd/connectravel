package com.connectravel.repository;

import com.connectravel.entity.*;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.slf4j.Logger;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccommodationRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(AccommodationRepositoryTest.class);

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private AccommodationImgRepository accommodationImgRepository;

    @Autowired
    private AccommodationOptionRepository accommodationOptionRepository;

    @Autowired
    private OptionRepository optionRepository;
    


    @BeforeEach
    public void setUp() {
        Member member = Member.builder()
                .name("TestName")
                .nickName("TestNickName")
                .email("TestEmail")
                .build();

        // Accommodation 정보 생성
        Accommodation accommodation = Accommodation.builder()
                .accommodationName("Test Accommodation")
                .postal(12345)
                .sellerName(member.getName())
                .email(member.getEmail())
                .address("123 Test Street, Test City")
                .count(0)
                .tel("123-456-7890")
                .accommodationType("Hotel")
                .build();

        // 이미지 2개 추가
        AccommodationImg img1 = new AccommodationImg();
        img1.setImgFile("image1.jpg");
        accommodation.addImage(img1);

        AccommodationImg img2 = new AccommodationImg();
        img2.setImgFile("image2.jpg");
        accommodation.addImage(img2);

        // 옵션 2개 추가
        Option option1 = Option.builder()
                .optionName("wifi")
                .build();

        Option option2 = Option.builder()
                .optionName("세탁기")
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


    @Test // Accommodation 등록 테스트
    public void saveAccommodationWithImagesAndOptions() {
        // Accommodation 정보를 불러옵니다.
        Accommodation savedAccommodation = accommodationRepository.findByEmail("TestEmail")
                .orElseThrow(() -> new RuntimeException("Failed to retrieve saved accommodation"));

        // 테스트 결과 확인
        assertNotNull(savedAccommodation.getAno());
        assertEquals(2, savedAccommodation.getImages().size());
        assertEquals(2, savedAccommodation.getAccommodationOptions().size());
        
        log.debug("Saved Accommodation ID: {}", savedAccommodation.getAno());
        savedAccommodation.getImages().forEach(img -> log.debug("Image: {}", img.getImgFile()));
        savedAccommodation.getAccommodationOptions().forEach(opt -> log.debug("Option: {}", opt.getOption().getOptionName()));
    }


    @Test // Accommodation 추가 테스트
    public void updateAccommodationWithImagesAndOptions() {
        // 1. 기존의 숙박시설을 불러옵니다.
        Accommodation loadedAccommodation = accommodationRepository.findById(1L).orElse(null);
        assertNotNull(loadedAccommodation, "Accommodation must exist to perform update test.");

        // 2. 숙박시설의 기본 정보 수정
        loadedAccommodation.setAccommodationName("Updated Accommodation Name");
        loadedAccommodation.setTel("987-654-3210");
        
        // 3. 이미지 추가
        AccommodationImg img3 = new AccommodationImg();
        img3.setImgFile("image3.jpg");
        loadedAccommodation.addImage(img3);

        // 옵션 추가
        Option option3 = Option.builder()
                .optionName("에어컨")
                .build();
        optionRepository.save(option3);
        AccommodationOption accommodationOption3 = new AccommodationOption();
        accommodationOption3.setAccommodation(loadedAccommodation);
        accommodationOption3.setOption(option3);
        loadedAccommodation.addAccommodationOption(accommodationOption3);

        // 4. 변경사항 저장 - AccommodationOption 이용 연관 관계 설정 후 DB에 저장
        Accommodation updatedAccommodation = accommodationRepository.save(loadedAccommodation);

        // 5. 검증
        assertEquals("Updated Accommodation Name", updatedAccommodation.getAccommodationName());
        assertEquals("987-654-3210", updatedAccommodation.getTel());
        assertEquals(3, updatedAccommodation.getImages().size());
        assertEquals(3, updatedAccommodation.getAccommodationOptions().size());

        log.debug("Updated Accommodation ID: {}", updatedAccommodation.getAno());
        updatedAccommodation.getImages().forEach(img -> log.debug("Image: {}", img.getImgFile()));
        updatedAccommodation.getAccommodationOptions().forEach(opt -> log.debug("Option: {}", opt.getOption().getOptionName()));
    }

    @Test // Accommodation 수정 테스트
    public void modifyExistingAccommodationWithImagesAndOptions() {
        // 1. 기존의 숙박시설을 불러옵니다.
        Accommodation loadedAccommodation = accommodationRepository.findByEmail("TestEmail").orElse(null);
        assertNotNull(loadedAccommodation, "Accommodation must exist to perform update test.");

        // 2. 숙박시설의 기본 정보 수정
        loadedAccommodation.setAccommodationName("Updated Accommodation Name");
        loadedAccommodation.setTel("987-654-3210");

        // 3. 기존의 이미지 수정
        // 예: 첫 번째 이미지의 파일명을 변경
        if (!loadedAccommodation.getImages().isEmpty()) {
            loadedAccommodation.getImages().get(0).setImgFile("updated_image1.jpg");
        }

        // 4. 기존의 옵션 수정
        // 예: 첫 번째 옵션의 이름을 변경
        if (!loadedAccommodation.getAccommodationOptions().isEmpty()) {
            loadedAccommodation.getAccommodationOptions().get(0).getOption().setOptionName("updated_option_name");
        }

        // 5. 변경사항 저장
        Accommodation updatedAccommodation = accommodationRepository.save(loadedAccommodation);

        // 6. 수정이 잘 이루어졌는지 검증
        assertEquals("Updated Accommodation Name", updatedAccommodation.getAccommodationName());
        assertEquals("987-654-3210", updatedAccommodation.getTel());
        if (!updatedAccommodation.getImages().isEmpty()) {
            assertEquals("updated_image1.jpg", updatedAccommodation.getImages().get(0).getImgFile());
        }
        if (!updatedAccommodation.getAccommodationOptions().isEmpty()) {
            assertEquals("updated_option_name", updatedAccommodation.getAccommodationOptions().get(0).getOption().getOptionName());
        }

        log.debug("Updated Accommodation ID: {}", updatedAccommodation.getAno());
        updatedAccommodation.getImages().forEach(img -> log.debug("Image: {}", img.getImgFile()));
        updatedAccommodation.getAccommodationOptions().forEach(opt -> log.debug("Option: {}", opt.getOption().getOptionName()));
    }

    @Test // Accommodation 삭제 테스트
    public void deleteAccommodationWithImagesAndOptions() {
        // 1. 삭제할 숙박시설 정보를 불러옵니다.
        Accommodation loadedAccommodation = accommodationRepository.findByEmail("TestEmail").orElse(null);
        assertNotNull(loadedAccommodation, "Accommodation must exist to perform delete test.");

        Long accommodationId = loadedAccommodation.getAno();

        // 2. 숙박시설 정보를 삭제합니다.
        accommodationRepository.delete(loadedAccommodation);

        // 3. 삭제 후 숙박시설 정보가 정말로 없는지 확인합니다.
        Optional<Accommodation> deletedAccommodation = accommodationRepository.findById(accommodationId);
        assertFalse(deletedAccommodation.isPresent(), "Deleted accommodation should not exist anymore.");
        
        List<AccommodationImg> imagesAfterDelete = accommodationImgRepository.findByAccommodationAno(accommodationId);
        assertTrue(imagesAfterDelete.isEmpty(), "All images related to the accommodation should be deleted.");

        List<AccommodationOption> accommodationOptionsAfterDelete = accommodationOptionRepository.findByAccommodationAno(accommodationId);
        assertTrue(accommodationOptionsAfterDelete.isEmpty(), "All options related to the accommodation should be deleted.");

    }

    @Test // Accommodation 특정 이미지를 삭제하는 테스트
    public void removeImageFromAccommodation() {
        // 1. 숙박시설을 불러옵니다.
        Accommodation loadedAccommodation = accommodationRepository.findByEmail("TestEmail").orElse(null);
        assertNotNull(loadedAccommodation, "Accommodation must exist to perform remove image test.");

        // 2. 첫 번째 이미지를 삭제합니다.
        if (!loadedAccommodation.getImages().isEmpty()) {
            AccommodationImg imageToRemove = loadedAccommodation.getImages().get(0);
            loadedAccommodation.removeImage(imageToRemove);
            accommodationImgRepository.delete(imageToRemove);  // 데이터베이스에서 이미지 삭제
        }

        // 3. 변경사항을 저장합니다.
        Accommodation updatedAccommodation = accommodationRepository.save(loadedAccommodation);

        // 4. 이미지가 제대로 삭제되었는지 확인합니다.
        assertEquals(1, updatedAccommodation.getImages().size());
    }

    @Test // Accommodation 특정 옵션을 삭제하는 테스트
    public void removeAccommodationOptionFromAccommodation() {
        // 1. 숙박시설을 불러옵니다.
        Accommodation loadedAccommodation = accommodationRepository.findByEmail("TestEmail").orElse(null);
        assertNotNull(loadedAccommodation, "Accommodation must exist to perform remove option test.");

        // 2. 첫 번째 옵션을 삭제합니다.
        if (!loadedAccommodation.getAccommodationOptions().isEmpty()) {
            AccommodationOption optionToRemove = loadedAccommodation.getAccommodationOptions().get(0);
            loadedAccommodation.removeAccommodationOption(optionToRemove);
            accommodationOptionRepository.delete(optionToRemove);  // 데이터베이스에서 옵션 삭제
        }

        // 3. 변경사항을 저장합니다.
        Accommodation updatedAccommodation = accommodationRepository.save(loadedAccommodation);

        // 4. 옵션이 제대로 삭제되었는지 확인합니다.
        assertEquals(1, updatedAccommodation.getAccommodationOptions().size());
    }

}
