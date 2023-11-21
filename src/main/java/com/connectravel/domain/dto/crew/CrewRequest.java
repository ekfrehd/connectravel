package com.connectravel.domain.dto.crew;

import com.connectravel.domain.entity.Accommodation;
import com.connectravel.domain.entity.Crew;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.chat.ChatRoom;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CrewRequest {


    private Long id;
    private String strict;
    private String title;
    private String content;
    private Integer crewLimit;
    private String datepick;
    private String timepick;
    private String chooseSport;
    private String imagePath;
    private Long accommodationAno;

    public CrewRequest(String strict, String title, String content, Integer crewLimit){
        this.strict = strict;
        this.title = title;
        this.content = content;
        this.crewLimit = crewLimit;

    }




    public Crew toEntity(Member user, Accommodation accommodation) {


        return Crew.builder()
                .strict(this.strict)
                .title(this.title)
                .content(this.content)
                .crewLimit(this.crewLimit)
                .datepick(this.datepick)
                .timepick(this.timepick)
                .accommodation(accommodation)
                .user(user)
                .imagePath(this.imagePath)
                .chatRoom(ChatRoom.builder().name(title).user(user).build())
                .user(user)
                .finish(0)
                .build();
    }

//    public SportEnum of(String value){
//        for(SportEnum sportEnum : SportEnum.values()){
//            if(sportEnum.getValue().equals(value)){
//                return sportEnum;
//            }
//        }
//        return null;
//    }


}
