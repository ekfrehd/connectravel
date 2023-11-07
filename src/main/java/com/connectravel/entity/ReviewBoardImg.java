package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "reviewBoard")
public class ReviewBoardImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    @Column(length = 200)
    private String imgFile;

    /* 연관 관계 */
    @ManyToOne
    private ReviewBoard reviewBoard;

}
