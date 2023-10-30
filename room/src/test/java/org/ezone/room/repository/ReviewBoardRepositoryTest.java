package org.ezone.room.repository;

import static org.junit.jupiter.api.Assertions.*;

import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
class ReviewBoardRepositoryTest {

    @Autowired
    private ReviewBoardRepository reviewBoardRepository;

    @Test
    @Transactional
    public void deleteBoard(){
        reviewBoardRepository.deleteById(1L);
    }




}