package com.connectravel.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AccommodationImgTest {

    private static final Logger log = LoggerFactory.getLogger(AccommodationImgTest.class);
    private Accommodation accommodation;
    private List<AccommodationImg> accommodationImgList;

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

        accommodationImgList = new ArrayList<>();

        AccommodationImg img1 = new AccommodationImg();
        img1.setImgFile("image1.jpg");
        accommodation.addImage(img1); // Assuming you have this method in Accommodation

        AccommodationImg img2 = new AccommodationImg();
        img2.setImgFile("image2.jpg");
        accommodation.addImage(img2); // Assuming you have this method in Accommodation

    }


    @Test
    public void testImageAssociation() {
        assertEquals(2, accommodationImgList.size());
        assertEquals("image1.jpg", accommodationImgList.get(0).getImgFile());
        assertEquals("image2.jpg", accommodationImgList.get(1).getImgFile());
        accommodationImgList.forEach(accommodationImg -> log.debug("AccommodationImg created: {}", accommodationImg));
    }
}
