package com.connectravel.service;

import com.connectravel.domain.entity.chat.Chat;
import com.connectravel.domain.entity.chat.ChatRoom;
import com.connectravel.dto.Response;
import com.connectravel.dto.chat.ChatMessageDTO;
import com.connectravel.exception.AppException;
import com.connectravel.exception.ErrorCode;
import com.connectravel.repository.chat.ChatRepository;
import com.connectravel.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    @Transactional
    public Response addChat(ChatMessageDTO chatMessageDTO){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(chatMessageDTO.getRoomId()).orElseThrow(()->new AppException(
                ErrorCode.DB_ERROR,""));
        Chat chater = chatRepository.save(chatMessageDTO.toChat(chatRoom));
        return Response.success(chater);
    }

    public List<ChatMessageDTO> listChat(Long id){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(id).orElseThrow(()->new AppException(ErrorCode.DB_ERROR,""));
        List<Chat> chats = chatRepository.findByChatRoom(chatRoom);
        List<ChatMessageDTO> list = new ArrayList<>();
        for(Chat chat : chats){
            ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder().message(chat.getMessage())
                    .writer(chat.getWriter())
                    .createdAt(chat.getRegTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .build();
            list.add(chatMessageDTO);
        }
        return list;
    }
}
