package com.connectravel.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaBoardDTO {
    
    private Long bno; //게시글 일련번호
    private String title; //게시글 제목
    private String content; //게시글 내용
    private String writerEmail; //작성자 이메일
    private String writerName; //작성자 이름
    private LocalDateTime regDate; //등록일
    private LocalDateTime modDate; //수정일
    private String createdBy; // 생성자
    private String modifiedBy; //수정자
    private int replyCount; //해당 게시글 댓글 수
}
