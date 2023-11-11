package com.connectravel.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "tourBoard")
public class TourBoardImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    private int ord;

    @Column(length = 200)
    private String imgFile;

    /* 연관 관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    private TourBoard tourBoard;

}