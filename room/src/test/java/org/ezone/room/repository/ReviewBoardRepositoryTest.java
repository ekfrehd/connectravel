package org.ezone.room.repository;

import static org.junit.jupiter.api.Assertions.*;

import groovy.util.logging.Slf4j;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.ezone.room.entity.Member;
import org.ezone.room.entity.Reservation;
import org.ezone.room.entity.ReviewBoard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
class ReviewBoardRepositoryTest {

    @Autowired
    private ReviewBoardRepository reviewBoardRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertReviewBoard(){
        LongStream.rangeClosed(1,100).forEach(i -> {


            Member member = Member.builder().build();
            memberRepository.save(member);
            ReviewBoard reviewBoard = ReviewBoard.builder()

                    .content("내용"+i)
                    .grade(i%5)
                    .rbno(i)
                    .member(member)


            .build();

            reviewBoardRepository.save(reviewBoard);
        });

    }


    @Test
    public void deleteBoard(){

        reviewBoardRepository.deleteById(6L);

    }




}