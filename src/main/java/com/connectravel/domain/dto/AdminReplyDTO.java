package com.connectravel.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminReplyDTO {

    private Long arno;

    private Long abno;

    private String content;

    private String replyer;

    private LocalDateTime regDate, modDate;

}