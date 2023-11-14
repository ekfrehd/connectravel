package com.connectravel.domain.entity;

import com.connectravel.domain.dto.RoomDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString(exclude = "accommodation")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @Column(length = 30,nullable = false)
    private String roomName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int minimumOccupancy; // 최소 인원

    @Column(nullable = false)
    private int maximumOccupancy; // 최대 인원

    private String content;

    private boolean operating; // 방의 운영 여부

    /* 연관 관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Accommodation accommodation;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomImg> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    /* 편의 메서드 */
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

    public void updateRoomDetails(RoomDTO roomDTO) {
        this.roomName = roomDTO.getRoomName();
        this.price = roomDTO.getPrice();
        this.minimumOccupancy = roomDTO.getMinimumOccupancy();
        this.maximumOccupancy = roomDTO.getMaximumOccupancy();
        this.content = roomDTO.getContent();
    }

}