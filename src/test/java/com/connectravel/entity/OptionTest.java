package com.connectravel.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OptionTest {

    private static final Logger log = LoggerFactory.getLogger(OptionTest.class);
    private Option option;
    private Accommodation accommodation;
    private AccommodationOption accommodationOption;

    @BeforeEach
    public void setUp() {
        accommodation = Accommodation.builder()
                .accommodationName("Test Hotel")
                .build();

        option = Option.builder()
                .optionName("Wi-Fi")
                .build();

        accommodationOption = new AccommodationOption();
        accommodationOption.setAccommodation(accommodation);
        accommodationOption.setOption(option);
    }

    @Test
    public void testOptionAssociation() {
        assertNotNull(accommodationOption.getOption());
        assertEquals("Wi-Fi", accommodationOption.getOption().getOptionName());
        log.debug("Option created: {} ", accommodationOption);
    }
}
