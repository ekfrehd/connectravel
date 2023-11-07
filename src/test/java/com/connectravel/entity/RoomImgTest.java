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

    private Room room;
    private List<RoomImg> roomImgList;

    @BeforeEach
    public void setUp() {
        room = new Room();
        room.setRoomName("Deluxe Suite");
        room.setPrice(1000);

        roomImgList = new ArrayList<>();

        RoomImg roomImg1 = new RoomImg();
        roomImg1.setImgFile("sample-image.jpg");
        room.addImage(roomImg1);
        roomImgList.add(roomImg1);

        RoomImg roomImg2 = new RoomImg();
        roomImg2.setImgFile("sample-image2.jpg");
        room.addImage(roomImg2);
        roomImgList.add(roomImg2);
    }

    @Test
    public void testRoomImgDetails() {
        assertEquals(2, roomImgList.size());
        assertEquals("sample-image.jpg", roomImgList.get(0).getImgFile());
        assertEquals("sample-image2.jpg", roomImgList.get(1).getImgFile());
        roomImgList.forEach(roomImg -> log.debug("RoomImg created: {}", roomImg));
    }

    @Test
    public void testRoomImgRemove() {
        // 이미지 제거
        room.removeImage(roomImgList.get(0));
        roomImgList.remove(0);

        // 제거 후 이미지 리스트의 크기가 1인지 확인
        assertEquals(1, roomImgList.size());

        // 제거 후 남아있는 이미지가 "sample-image2.jpg"인지 확인
        assertEquals("sample-image2.jpg", roomImgList.get(0).getImgFile());

    }
}
