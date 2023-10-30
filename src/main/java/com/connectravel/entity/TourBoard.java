package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TourBoard extends BaseEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long tbno; // 번호

    @Column (length = 100, nullable = false)
    private String title; // 제목

    @Column (length = 2000, nullable = false)
    private String content; // 내용

    private String region; // 지역

    private String category; // 카테고리

    private double grade; // 평점

    private int reviewCount; // 리뷰수

    @Column (nullable = false)
    private int postal; // 우편번호

    @Column (nullable = false)
    private String address; // 주소

    public void changeTitle (String title) {
        this.title = title;
    }

    public void changeContent (String content) {
        this.content = content;
    }

    public void setGrade (double grade) { // 평점 변경
        this.grade = grade;
    }

    public void setReviewCount (int reviewCount) { // 리뷰 수 변경
        this.reviewCount = reviewCount;
    }
}

