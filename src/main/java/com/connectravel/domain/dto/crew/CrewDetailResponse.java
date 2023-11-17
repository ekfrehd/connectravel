package com.connectravel.domain.dto.crew;

import com.connectravel.domain.entity.Crew;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CrewDetailResponse {
    private Long id;
    private String strict;
    private String title;
    private String content;
    private Integer crewLimit;
    private Long userId;
    private String userName;
    private String nickName;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
//    private SportEnum sportEnum;
    private Long chatRoomId;
    private String imagePath;
    private String profileImagePath;
    private String date;
    private Integer finish;
    public static CrewDetailResponse of(Crew crew) {
        return CrewDetailResponse.builder()
                .id(crew.getId())
                .strict(crew.getStrict())
                .title(crew.getTitle())
                .content(crew.getContent())
                .crewLimit(crew.getCrewLimit())
                .userId(crew.getUser().getId())
                .nickName(crew.getUser().getNickName())
                .userName(crew.getUser().getUsername())
                .createdAt(crew.getRegTime())
                .lastModifiedAt(crew.getModTime())
                .imagePath(crew.getImagePath())
//                .profileImagePath(crew.getUser().getImagePath())
                .chatRoomId(crew.getChatRoom().getRoomId())
//                .sportEnum(crew.getSportEnum())
                .date(crew.getDatepick()+" "+crew.getTimepick())
                .finish(crew.getFinish())
                .build();
    }

}
