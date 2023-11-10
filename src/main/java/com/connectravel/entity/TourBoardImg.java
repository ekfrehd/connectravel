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
@ToString(exclude = "tourBoard")
public class TourBoardImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    private int ord;

    @Column(length = 200)
    private String imgFile;

    /* 연관 관계 */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TourBoard tourBoard;

}
