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
@ToString(exclude = "tourBoard") //FK지정 - 외래키로 설정될 엔티티 테이블 이름을 exclude 속성 지정해줌
public class TourBoardReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tbrno;

    private String content;

    private double grade;

    private int recommend;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩 설정
    @OnDelete(action = OnDeleteAction.CASCADE) // 글을 지우면 댓글도 같이 사라짐
    private TourBoard tourBoard; //연관관계 지정

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member; // id

    public void changeContent(String content) {this.content = content;}

    public void changeGrade(Double grade) {this.grade = grade;}
}
