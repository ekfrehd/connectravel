package org.ezone.room.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;
import org.ezone.room.repository.ReviewBoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReviewBoardServiceImplTest {

    @Autowired
    ReviewBoardRepository reviewBoardRepository;




    @Test
    @Transactional
    public void removeWithReplies(Long rbno) {
        rbno = 1L;
        reviewBoardRepository.deleteById(rbno);
    }


}