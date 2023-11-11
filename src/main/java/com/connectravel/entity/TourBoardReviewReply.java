package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"tourBoardReview","member"})
public class TourBoardReviewReply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tbrrno;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private TourBoardReview tourBoardReview;

    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;

    public void changeContent(String content) {this.content = content;}
}
