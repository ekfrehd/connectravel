package com.connectravel.entity;

import com.connectravel.entity.RoomImg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomImgTest {

    private static final Logger log = LoggerFactory.getLogger(RoomImgTest.class);

    private List<RoomImg> roomImgList;

    @BeforeEach
    public void setUp() {
        roomImgList = new ArrayList<>();

        RoomImg roomImg1 = new RoomImg();
        roomImg1.setImgFile("sample-image.jpg");
        roomImgList.add(roomImg1);

        RoomImg roomImg2 = new RoomImg();
        roomImg2.setImgFile("sample-image2.jpg");
        roomImgList.add(roomImg2);
    }

    @Test
    public void testRoomImgDetails() {
        assertEquals(2, roomImgList.size());
        assertEquals("sample-image.jpg", roomImgList.get(0).getImgFile());
        assertEquals("sample-image2.jpg", roomImgList.get(1).getImgFile());

        roomImgList.forEach(roomImg -> log.debug("RoomImg created: {}", roomImg));
    }
}
