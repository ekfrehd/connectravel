package com.connectravel.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "tourBoardReview")
public class TourBoardReviewImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    @Column(length = 200)
    private String imgFile;

    /* 연관 관계 */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TourBoardReview tourBoardReview;

}
