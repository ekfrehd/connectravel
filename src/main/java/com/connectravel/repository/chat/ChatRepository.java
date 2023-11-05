package com.connectravel.repository.chat;

import com.connectravel.domain.entity.chat.Chat;
import com.connectravel.domain.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    List<Chat> findByChatRoom(ChatRoom chatRoom);
}

