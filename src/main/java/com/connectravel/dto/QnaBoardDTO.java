package com.connectravel.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaBoardDTO {

    //validation.NotBlank를 이용해 유효성 검증

    private Long bno; //게시글 일련번호
    @NotBlank (message = "제목을 입력해주세요.")
    private String title; //게시글 제목
    @NotBlank (message = "내용을 입력해주세요.")
    private String content; //게시글 내용
    private String writerEmail; //작성자 이메일
    private String writerName; //작성자 이름
    private LocalDateTime regDate, modDate; // 등록일, 수정일
    private String createdBy, modifiedBy; // 생성자, 수정자
    private int replyCount; //해당 게시글 댓글 수
}
