package com.connectravel.domain.entity;

import com.connectravel.domain.dto.crew.CrewRequestDTO;
import com.connectravel.domain.entity.chat.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Where(clause = "deleted_at is null")
public class Crew extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String strict;
    private String roadName;
    private String title;
    private String content;
    private Integer crewLimit;

    private String imagePath;

    private String datepick;
    private String timepick;
    private Integer finish;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="room_id")
    private ChatRoom chatRoom;

//    @Enumerated(value = EnumType.STRING)
//    private SportEnum sportEnum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member user;


    //참여중인사람 조회
    @OneToMany(mappedBy = "crew")
    private List<Participation> participations = new ArrayList<>();


    public void setFinish(Integer finish){
        this.finish = finish;
    }
    public void setChatRoom(ChatRoom chatRoom){
        this.chatRoom = chatRoom;
    }

    public void setParticipations(List<Participation> participations){
        this.participations = participations;
    }

    public void of(CrewRequestDTO request) {
        this.strict = request.getStrict();
        this.title = request.getTitle();
        this.content = request.getContent();
        this.crewLimit = request.getCrewLimit();
    }
}
