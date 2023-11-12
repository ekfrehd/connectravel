package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewBoard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rbno;

    @Column(length = 1500, nullable = false)
    private String content;
    private double grade;

    /* 연관 관계 */
    @Builder.Default
    @OneToMany(mappedBy = "reviewBoard", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReviewReply> replies = new ArrayList<>();

    @ManyToOne
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /* 도메인 로직 */
    public void changeContent(String content){
        this.content = content;
    }

} 
