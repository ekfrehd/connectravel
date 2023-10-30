package com.connectravel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QnaReplyDTO {

    private Long rno; // 댓글 일련번호
    @NotBlank (message = "내용을 입력해주세요.")
    private String text; // 댓글 내용
    private String replyer; // 댓글 작성자
    private Long bno; // 게시글 일련번호
    private LocalDateTime regDate, modDate; // 댓글 작성일, 수정일

}
