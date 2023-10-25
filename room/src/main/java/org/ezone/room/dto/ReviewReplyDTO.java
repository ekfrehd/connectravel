package org.ezone.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ezone.room.entity.ReviewReply;

import java.time.LocalDateTime;
import java.util.List;

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
