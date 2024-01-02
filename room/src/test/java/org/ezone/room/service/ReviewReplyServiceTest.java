package org.ezone.room.service;


import java.time.LocalDateTime;
import java.util.List;
import org.ezone.room.dto.PageRequestDTO;
import org.ezone.room.dto.PageResultDTO;
import org.ezone.room.dto.ReviewReplyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReviewReplyServiceTest {

    @Autowired
    private ReviewReplyServiceImpl replyService;

    @Test
    public void testRegister(){

        ReviewReplyDTO dto = ReviewReplyDTO.builder()
                        .rbno(5L).text("댓글 테스트").replyer("asd@asd")
                        .regDate(LocalDateTime.now())
                                .modDate(LocalDateTime.now())

                .build();

        Long id = replyService.register(dto);
    }

    @Test
    public void testList(){

        List<ReviewReplyDTO> result = replyService.getList(5L);

        for(ReviewReplyDTO reviewReplyDTO : result.stream().toList()){
            System.out.println(reviewReplyDTO);
        }

    }
}
