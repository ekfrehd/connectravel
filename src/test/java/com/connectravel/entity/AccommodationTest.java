package com.connectravel.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccommodationTest {

    private static final Logger log = LoggerFactory.getLogger(AccommodationTest.class);

    private Accommodation accommodation;

    @BeforeEach
    public void setUp() {
        accommodation = Accommodation.builder()
                .name("Test Hotel")
                .postal(12345)
                .adminName("John Doe")
                .address("123 Test St")
                .accommodationType("Hotel")
                .tel("010-1234-5678")
                .content("Welcome to Test Hotel")
                .build();
    }

    @Test
    public void testAccommodationCreation() {
        assertNotNull(accommodation);
        assertEquals("Test Hotel", accommodation.getName());
        assertEquals("John Doe", accommodation.getAdminName());
        log.debug("Accommodation created: {}", accommodation);
    }
}
