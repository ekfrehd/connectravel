package org.ezone.room.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewReplyDTO {

    private Long rrno;
    private String text;
    private String replyer;
    private Long rbno;
    private LocalDateTime regDate, modDate;

}
