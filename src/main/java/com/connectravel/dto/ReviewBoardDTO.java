package com.connectravel.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    private String content;
    private double grade;
    private int replyCount; //해당 게시글 댓글 수
    private LocalDateTime regDate, modDate;

    private String roomName;
    private String writerEmail;
    private String writerName;

    private List<ReviewReplyDTO> replies;
}
