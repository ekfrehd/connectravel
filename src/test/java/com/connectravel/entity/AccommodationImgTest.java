package com.connectravel.entity;

import com.connectravel.domain.entity.Accommodation;
import com.connectravel.domain.entity.AccommodationImg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AccommodationImgTest {

    private static final Logger log = LoggerFactory.getLogger(AccommodationImgTest.class);
    private Accommodation accommodation;
    private List<AccommodationImg> accommodationImgList;

    @BeforeEach
    public void setUp() {
        accommodation = Accommodation.builder()
                .accommodationName("Test Hotel")
                .postal(12345)
                .sellerName("John Doe")
                .address("123 Test St")
                .tel("010-1234-5678")
                .content("Welcome to Test Hotel")
                .build();

        accommodationImgList = new ArrayList<>();

        AccommodationImg img1 = new AccommodationImg();
        img1.setImgFile("image1.jpg");
        accommodation.addImage(img1);
        accommodationImgList.add(img1);

        AccommodationImg img2 = new AccommodationImg();
        img2.setImgFile("image2.jpg");
        accommodation.addImage(img2);
        accommodationImgList.add(img2);

    }

    @Test
    public void testAccImgDetails() {
        assertEquals(2, accommodationImgList.size());
        assertEquals("image1.jpg", accommodationImgList.get(0).getImgFile());
        assertEquals("image2.jpg", accommodationImgList.get(1).getImgFile());
        accommodationImgList.forEach(accommodationImg -> log.debug("AccommodationImg created: {}", accommodationImg));
    }

    @Test
    public void testAccImgRemove() {
        // 먼저 이미지를 제거합니다.
        accommodation.removeImage(accommodationImgList.get(0));
        accommodationImgList.remove(0); // accommodationImgList에서도 삭제합니다.

        // 제거 후 이미지 리스트의 크기가 1인지 확인합니다.
        assertEquals(1, accommodationImgList.size());

        // 제거 후 남아있는 이미지가 "image2.jpg"인지 확인합니다.
        assertEquals("image2.jpg", accommodationImgList.get(0).getImgFile());
    }

}
