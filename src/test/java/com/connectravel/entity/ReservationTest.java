package com.connectravel.entity;

import com.connectravel.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {

    private static final Logger log = LoggerFactory.getLogger(ReservationTest.class);
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        reservation = new Reservation();
        // ... initialize Room, Member, and other fields as needed
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusDays(3));
        // ... set other fields as needed
    }

    @Test
    public void testReservationDates() {
        assertEquals(LocalDate.now(), reservation.getStartDate());
        assertEquals(LocalDate.now().plusDays(3), reservation.getEndDate());
        log.debug("Reservation created: {}", reservation);
    }
}
