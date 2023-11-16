package com.connectravel.service;

import com.connectravel.domain.dto.Response;
import com.connectravel.domain.dto.check.CheckRequest;
import com.connectravel.domain.dto.check.CheckResponse;
import com.connectravel.domain.entity.Member;

import com.connectravel.domain.entity.chat.Chat;
import com.connectravel.domain.entity.chat.ChatRoom;
import com.connectravel.domain.entity.check.ChatConfigEntity;
import com.connectravel.exception.AppException;
import com.connectravel.exception.ErrorCode;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.chat.ChatConfigRepository;
import com.connectravel.repository.chat.ChatRepository;
import com.connectravel.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatConfigService {
    private final ChatConfigRepository chatConfigRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository userRepository;
    private final ChatRepository chatRepository;
    //읽었던 내용까지 저장하는 로직

    @Transactional
    public Response checkSave(CheckRequest checkRequest){


        Long roomId = checkRequest.getRoomId();
        String userName = checkRequest.getUserName();
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(()->new AppException(ErrorCode.DB_ERROR,ErrorCode.DB_ERROR.getMessage()));
        Member user = userRepository.findByUsername(userName).orElseThrow(()->new AppException(ErrorCode.USERID_NOT_FOUND,ErrorCode.USERID_NOT_FOUND.getMessage()));

        if(chatConfigRepository.existsByUserAndChatRoom(user,chatRoom)){
            ChatConfigEntity chatConfigEntity = chatConfigRepository.findByUserAndChatRoom(user,chatRoom).orElseThrow(()->new AppException(ErrorCode.DB_ERROR,ErrorCode.DB_ERROR.getMessage()));
            chatConfigEntity.setTime();
            return Response.success(chatConfigEntity.getConfigTime());
        }else{
            ChatConfigEntity chatConfigEntity = ChatConfigEntity.builder().user(user).chatRoom(chatRoom).configTime(LocalDateTime.now()).build();
            chatConfigRepository.save(chatConfigEntity);
            return Response.success(chatConfigEntity.getConfigTime());
        }
    }

    public Response checkRead(Long roomId, String userName){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(()->new AppException(ErrorCode.DB_ERROR,ErrorCode.DB_ERROR.getMessage()));
        Member user = userRepository.findByUsername(userName).orElseThrow(()->new AppException(ErrorCode.USERID_NOT_FOUND,ErrorCode.USERID_NOT_FOUND.getMessage()));
        int index = 0;
        if(chatConfigRepository.existsByUserAndChatRoom(user,chatRoom)){
            ChatConfigEntity check = chatConfigRepository.findByUserAndChatRoom(user,chatRoom).orElseThrow(()->new AppException(ErrorCode.DB_ERROR,ErrorCode.DB_ERROR.getMessage()));
            List<Chat> chats = chatRepository.findByChatRoom(chatRoom);
            for(Chat chat : chats){
                if(chat.getRegTime().isBefore(check.getConfigTime())){
                    index++;
                }
            }
            return Response.success(CheckResponse.builder().index(index).build());
        }else{
            return Response.success(CheckResponse.builder().index(index).build());
        }

    }

}
