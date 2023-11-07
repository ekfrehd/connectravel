package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "member")
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;
    @Column(nullable = false)
    private String accommodationName;

    @Column(nullable = false)
    private int postal;
    @Column(nullable = false)
    private String sellerName;
    @Column(nullable = false)
    private String address;
    @Column(scale = 0)
    private int count; // 숙소 예약 횟수

    private String accommodationType; // 숙소 종류

    private String region;

    @Column(nullable = false)
    private String tel;

    @Column(length = 5000)
    private String content; // 기본 정보, 편의시설 및 서비스, 판매자 정보

    private String email;
    private String intro; // "사장님 한마디" 같은 짧은 숙소 소개글

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccommodationImg> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccommodationOption> accommodationOptions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Room> rooms = new ArrayList<>();

    /* 편의 메서드 */
    public void addImage(AccommodationImg img) {
        images.add(img);
        img.setAccommodation(this);
    }

    public void removeImage(AccommodationImg img) {
        images.remove(img);
        img.setAccommodation(null);
    }

    public void addAccommodationOption(AccommodationOption accommodationOption) {
        accommodationOptions.add(accommodationOption);
        accommodationOption.setAccommodation(this);
    }

    public void removeAccommodationOption(AccommodationOption accommodationOption) {
        accommodationOptions.remove(accommodationOption);
        accommodationOption.setAccommodation(null);
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
        room.setAccommodation(this);
    }


}
