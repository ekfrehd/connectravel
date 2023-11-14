package com.connectravel.service;

import com.connectravel.domain.entity.Crew;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.Participation;
import com.connectravel.domain.entity.chat.ChatRoom;
import com.connectravel.dto.chat.ChatRoomDTO;
import com.connectravel.exception.AppException;
import com.connectravel.exception.ErrorCode;
import com.connectravel.repository.CrewRepository;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.ParticipationRepository;
import com.connectravel.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final CrewRepository crewRepository;
    private final ParticipationRepository participationRepository;

    private final MemberRepository userRepository;

    public List<ChatRoomDTO> findAllRooms(){
        List<ChatRoom> result = chatRoomRepository.findAll();
        List<ChatRoomDTO> list =  ChatRoomDTO.createList(result);
        return list;
    }

//    public List<ChatRoom> findBy(String userName){
//        Member user = userRepository.findByName(userName).orElseThrow(()->new AppException(ErrorCode.USERID_NOT_FOUND,ErrorCode.USERID_NOT_FOUND.getMessage()));
//        List<ChatRoom> list = user.getChatRooms();
//        return list;
//    }


    public List<ChatRoom> findByParticipation(String userName){
        //참여 여부로 채팅방 조회
        Member user = userRepository.findByUsername(userName).orElseThrow(()->new AppException(ErrorCode.USERID_NOT_FOUND,ErrorCode.USERID_NOT_FOUND.getMessage()));
        List<ChatRoom> chatRoomList = new ArrayList<>();
        List<Participation> participationList = participationRepository.findByUser(user);
        for(Participation p : participationList){
            chatRoomList.add(p.getCrew().getChatRoom());
        }
        return chatRoomList;
    }

    public ChatRoom findRoomById(Long id){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(id).orElseThrow(()->new AppException(ErrorCode.DB_ERROR,""));
        return chatRoom;
    }

    @Transactional
    public ChatRoom createChatRoomDTO(ChatRoomDTO chatRoomDTO, String userName){
        Member user = userRepository.findByUsername(userName).orElseThrow(()->new AppException(ErrorCode.USERID_NOT_FOUND,ErrorCode.USERID_NOT_FOUND.getMessage()));
        Crew crew = crewRepository.findById(chatRoomDTO.getCrewId()).orElseThrow(()->new AppException(ErrorCode.CREW_NOT_FOUND,ErrorCode.CREW_NOT_FOUND.getMessage()));
        ChatRoom chatRoom = chatRoomDTO.of(user);
        crew.setChatRoom(chatRoom);
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
        return savedRoom;
    }
}
