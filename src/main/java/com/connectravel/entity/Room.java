package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//방 엔티티
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "accommodation")

public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;  //방번호

    @Column(length = 30, nullable = false)
    private String roomName; //방 이름
    @Column(nullable = false)
    private int price; //방 갸격

    private boolean operating;

    private String content;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Accommodation accommodation; //어디 소속 숙소의 방인지 알아야되니까 중계어플에서는 필수.

}
