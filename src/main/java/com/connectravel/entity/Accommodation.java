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
@ToString(exclude = "member") //FK지정(One쪽의 테이블에서는 외래키 연결시킬 자신의 엔티티의 컬럼명을 exclude 지정)
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;
    @Column(nullable = false)
    private String name; // 숙소이름

    @Column(nullable = false)
    private int postal; // 우편번호
    @Column(nullable = false)
    private String adminName; // 숙소 운영자 이름
    @Column(nullable = false)
    private String address; // 주소
    @Column(scale = 0)
    private int count; // 숙소 예약 횟수

    private String accommodationType; // 숙소 종류

    private String region; // 지역

    @Column(nullable = false)
    private String tel; // 전화번호

    @Column(length = 5000)
    private String content; // 숙박업소소개

    private String email; // 이메일
    private String intro; // 소개

    //지연 로딩 지정
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
    @OneToMany(mappedBy = "accommodation", fetch = FetchType.LAZY)
    private List<Room> rooms = new ArrayList<>();

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


}
