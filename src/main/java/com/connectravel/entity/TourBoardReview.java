package com.connectravel.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"tourBoard", "images"})
public class TourBoardReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tbrno;
    private String content;
    private double grade;
    private int recommend; // 추천수

    // 삭제 여부 필드 추가
    @Builder.Default
    private boolean deleted = false;

    /* 연관 관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    private TourBoard tourBoard;

    @Builder.Default
    @OneToMany(mappedBy = "tourBoardReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourBoardReviewReply> replies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "tourBoardReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourBoardReviewImg> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /* 도메인 로직 */
    public void changeContent(String content) {this.content = content;}

    public void changeGrade(Double grade) {this.grade = grade;}
}
