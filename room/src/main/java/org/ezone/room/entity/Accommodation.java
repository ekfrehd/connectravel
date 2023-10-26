package org.ezone.room.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString(exclude = "member") //FK지정(One쪽의 테이블에서는 외래키 연결시킬 자신의 엔티티의 컬럼명을 exclude 지정)
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;
    @Column(nullable = false)
    private String name; // 숙소이름
    @Column(scale = 4)
    private double grade; // 숙소 평점
    @Column(nullable = false)
    private int postal; // 우편번호
    @Column(nullable = false)
    private String adminName; // 숙소 운영자 이름
    @Column(nullable = false)
    private String address; // 주소
    @Column(scale = 0)
    private int count; // 숙소 예약 횟수

    private String category; // 숙소 종류

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

    private int reviewCount; // 리뷰수

    public void setGrade(double grade) { // 평점 변경
        this.grade = grade;
    }

    public void setReviewCount(int reviewcount) { // 리뷰 수 변경
        this.reviewCount = reviewcount;
    }


}
