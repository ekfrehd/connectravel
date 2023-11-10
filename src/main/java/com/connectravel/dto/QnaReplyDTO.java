package com.connectravel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaReplyDTO {

    private Long qrno;
    private Long qbno;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private String replyer;
    private LocalDateTime regDate, modDate;

}
