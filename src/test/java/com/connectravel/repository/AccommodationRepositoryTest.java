package com.connectravel.repository;

import com.connectravel.entity.Accommodation;
import com.connectravel.entity.AccommodationImg;
import com.connectravel.entity.AccommodationOption;
import com.connectravel.entity.Option;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccommodationRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(AccommodationRepositoryTest.class);

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Test
    public void saveAccommodationWithImagesAndOptions() {
        // Create Accommodation
        Accommodation accommodation = Accommodation.builder()
                .name("Test Accommodation")
                .postal(12345)
                .adminName("Admin Test")
                .address("123 Test Street, Test City")
                .count(0)
                .tel("123-456-7890")
                .accommodationType("Hotel")
                .build();

        // Optional fields can be set as well if needed
        // accommodation.setAccommodationType("Type");
        // accommodation.setRegion("Region");
        // accommodation.setContent("Content");
        // accommodation.setEmail("test@email.com");
        // accommodation.setIntro("Intro");

        AccommodationImg img1 = new AccommodationImg();
        img1.setImgFile("image1.jpg");
        accommodation.addImage(img1);

        AccommodationImg img2 = new AccommodationImg();
        img2.setImgFile("image2.jpg");
        accommodation.addImage(img2);

        // Create and Save Option
        Option option1 = Option.builder()
                .optionCategory("공용")
                .optionName("wifi")
                .build();

        Option option2 = Option.builder()
                .optionCategory("공용")
                .optionName("세탁기")
                .build();
        // ... set option fields ...
        optionRepository.save(option1);
        optionRepository.save(option2);

        // Associate Option with Accommodation using AccommodationOption
        AccommodationOption accommodationOption1 = new AccommodationOption();
        accommodationOption1.setAccommodation(accommodation);
        accommodationOption1.setOption(option1);
        accommodation.addAccommodationOption(accommodationOption1);

        // Save Accommodation
        Accommodation savedAccommodation = accommodationRepository.save(accommodation);

        // Assertions
        assertNotNull(savedAccommodation.getAno());
        assertEquals(2, savedAccommodation.getImages().size());
        assertEquals(1, savedAccommodation.getAccommodationOptions().size());

        log.debug("Saved Accommodation ID: {}", savedAccommodation.getAno());
        savedAccommodation.getImages().forEach(img -> log.debug("Image: {}", img.getImgFile()));
        savedAccommodation.getAccommodationOptions().forEach(opt -> log.debug("Option: {}", opt.getOption().getOptionName()));
        savedAccommodation.getAccommodationOptions().forEach(opt -> log.debug("Option: {}", opt.getOption().getOptionCategory()));

    }

}
