package com.connectravel.repository.chat;

import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.chat.ChatRoom;
import com.connectravel.domain.entity.check.ChatConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatConfigRepository extends JpaRepository<ChatConfigEntity, Long> {
    Optional<ChatConfigEntity> findByUserAndChatRoom(Member user, ChatRoom chatRoom);
    Boolean existsByUserAndChatRoom(Member user, ChatRoom chatRoom);
}
