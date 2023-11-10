package com.connectravel.domain.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "reviewBoard")
public class ReviewBoardImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // delete 옵션을 cascade
    private ReviewBoard reviewBoard;

    @Column(length = 200)
    private String imgFile;
}