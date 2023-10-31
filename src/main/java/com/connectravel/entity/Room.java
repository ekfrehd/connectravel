package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//방 엔티티
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "accommodation")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @Column(length = 30,nullable = false)
    private String roomName;
    @Column(nullable = false)
    private int price;

    private boolean operating;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Accommodation accommodation;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomImg> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    public void addImage(RoomImg img) {
        images.add(img);
        img.setRoom(this);
    }

    public void removeImage(RoomImg img) {
        images.remove(img);
        img.setRoom(null);
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setRoom(this);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.setRoom(null);
    }

}
