package com.connectravel.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString(exclude = "member")
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;

    @Column(nullable = false)
    private String accommodationName;
    private String accommodationType; // 숙소 종류
    @Column(nullable = false)
    private String sellerName;
    private String sellerEmail;

    @Column(nullable = false)
    private int postal;
    private String region;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String tel;

    @Column(length = 5000)
    private String content; // 기본 정보, 편의시설 및 서비스, 판매자 정보
    private String intro; // "사장님 한마디" 같은 짧은 숙소 소개글

    @Column(scale = 0)
    private int count; // 숙소 예약 횟수
    private int reviewCount; // 리뷰수
    @Column(scale = 4)
    private double grade; // 숙소 평점

    /* 연관 관계 */
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

    /* 도메인 로직 - 이미지 */
    public void addImage(AccommodationImg img) {
        images.add(img);
        img.setAccommodation(this);
    }
    public void removeImage(AccommodationImg img) {
        images.remove(img);
        img.setAccommodation(null);
    }

    /* 도메인 로직 - 옵션 */
    public void addAccommodationOption(AccommodationOption accommodationOption) {
        accommodationOptions.add(accommodationOption);
        accommodationOption.setAccommodation(this);
    }

    /* 도메인 로직 - 방 */
    public void addRoom(Room room) {
        this.rooms.add(room);
        room.setAccommodation(this);
    }

    /* 도메인 로직 - 리뷰 */
    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

}