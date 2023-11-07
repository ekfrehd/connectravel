package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"tourBoardReview","member"}) //FK지정 - 외래키로 설정될 엔티티 테이블 이름을 exclude 속성 지정해줌
public class TourBoardReviewReply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tbrrno;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩 설정
    private TourBoardReview tourBoardReview; //연관관계 지정

    @ManyToOne(fetch = FetchType.EAGER)
    private Member member; // id

    public void changeText(String text) {this.text = text;} // 대댓글 수정
}
