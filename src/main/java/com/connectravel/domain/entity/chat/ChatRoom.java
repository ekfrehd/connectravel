package com.connectravel.domain.entity.chat;

import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.check.ChatConfigEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "UPDATE chat_room SET deleted_at = now()  WHERE id=?")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private String name;
    private LocalDateTime deletedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    private Member user;


    @OneToMany(mappedBy = "chatRoom")
    private List<ChatConfigEntity> chatConfigEntities = new ArrayList<>();
//    @Builder.Default
//    @OneToMany(mappedBy = "chat_room")
//    private List<Chat> chats = new ArrayList<>();

}
