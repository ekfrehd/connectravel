package com.connectravel.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

    private String title;
    private String body;

    private Integer status;

    private LocalDateTime deletedAt;

    public static Participation deleteParticipation(Participation beforeParticipation, LocalDateTime localDateTime){

        return Participation.builder()
                .id(beforeParticipation.id)
                .user(beforeParticipation.user)
                .crew(beforeParticipation.crew)
                .title(beforeParticipation.title)
                .body(beforeParticipation.body)
                .status(beforeParticipation.status)
                .deletedAt(localDateTime)
                .build();


    }

}
