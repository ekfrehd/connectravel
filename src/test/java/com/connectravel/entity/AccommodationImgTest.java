package com.connectravel.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AccommodationImgTest {

    private static final Logger log = LoggerFactory.getLogger(AccommodationImgTest.class);
    private Accommodation accommodation;
    private AccommodationImg accommodationImg;

    @BeforeEach
    public void setUp() {
        accommodation = Accommodation.builder()
                .name("Test Hotel")
                .postal(12345)
                .adminName("John Doe")
                .address("123 Test St")
                .tel("010-1234-5678")
                .content("Welcome to Test Hotel")
                .build();

        accommodationImg = AccommodationImg.builder()
                .accommodation(accommodation)
                .imgFile("test.jpg")
                .build();
    }

    @Test
    public void testImageAssociation() {
        assertNotNull(accommodationImg.getAccommodation());
        assertEquals("Test Hotel", accommodationImg.getAccommodation().getName());
        log.debug("Accommodation created: {}", accommodationImg);
    }
}
