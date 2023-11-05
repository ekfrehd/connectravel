package com.connectravel.entity;

import com.connectravel.entity.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    private static final Logger log = LoggerFactory.getLogger(RoomTest.class);
    private Room room;

    @BeforeEach
    public void setUp() {
        room = new Room();
        room.setRoomName("Deluxe Suite");
        room.setPrice(1000);
        // ... set other fields as needed
    }

    @Test
    public void testRoomDetails() {
        assertEquals("Deluxe Suite", room.getRoomName());
        assertEquals(1000, room.getPrice());
        log.debug("Room created: {}", room);
    }
}
