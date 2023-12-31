package org.ezone.room.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;
import org.ezone.room.dto.ReviewBoardDTO;
import org.ezone.room.repository.ReviewBoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class ReviewBoardServiceImplTest {

    @Autowired
    private ReviewBoardServiceImpl reviewBoardService;



    @Test
    @Transactional
    public void testRemove() {
        Long rbno = 4L;
        reviewBoardService.remove(rbno);
    }


    @Test
    public void testModify() {

        ReviewBoardDTO dto = ReviewBoardDTO.builder()
                .rbno(106L)
                .grade(5d)
                .content("ModifyTest...")
                .rno(1L)
                .ano(1L)
                .rvno(1L)
                .writerEmail("asd@asd")  //현재 데이터베이스에 존재하는 회원 이메일
                .build();


        reviewBoardService.modify(dto);

    }


}