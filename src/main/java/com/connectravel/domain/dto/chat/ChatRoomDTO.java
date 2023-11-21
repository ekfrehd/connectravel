package com.connectravel.domain.dto.chat;


import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.chat.ChatRoom;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ChatRoomDTO {

    private Long roomId;
    private String name;
    private Long crewId;

    public static List<ChatRoomDTO> createList(List<ChatRoom> list){
        List<ChatRoomDTO> result = new ArrayList<>();
        for(ChatRoom chatRoom : list){
            result.add(ChatRoomDTO.builder().roomId(chatRoom.getRoomId())
                    .name(chatRoom.getName())
                    .build());
        }
        return result;
    }

    public ChatRoom of(Member user){
        return ChatRoom.builder().name(name).user(user).build();
    }

}