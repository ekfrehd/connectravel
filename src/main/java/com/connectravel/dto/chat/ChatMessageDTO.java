package com.connectravel.dto.chat;

import com.connectravel.domain.entity.chat.Chat;
import com.connectravel.domain.entity.chat.ChatRoom;
import lombok.*;

import java.util.List;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {


    private Long roomId;
    private String writer;

    private String message;

    private String createdAt;
    private List<String> userList;
    private Integer state;

    public Chat toChat(ChatRoom chatRoom){
        System.out.println(writer);
        return Chat.builder().message(message)
                .writer(writer)
                .chatRoom(chatRoom)
                .isChecked(false)
                .build();
    }

}