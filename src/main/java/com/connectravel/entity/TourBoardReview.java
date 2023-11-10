package com.connectravel.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "tourBoard")
public class TourBoardReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tbrno;
    private String content;
    private double grade;
    private int recommend;

    /* 연관 관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TourBoard tourBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /* 도메인 로직 */
    public void changeContent(String content) {this.content = content;}

    public void changeGrade(Double grade) {this.grade = grade;}
}
