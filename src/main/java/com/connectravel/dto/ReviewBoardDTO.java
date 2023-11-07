package com.connectravel.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewBoardDTO {

    private Long rbno; // 게시물 번호
    private Long ano; // 숙소번호
    private Long rno; // 방번호
    private Long rvno; // 예약번호
    private double grade;
    private String content;
    private String writerEmail; //작성자 이메일
    private String writerName;
    private String roomName;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int replyCount; //해당 게시글 댓글 수
    private List<ReviewReplyDTO> replies;
}
