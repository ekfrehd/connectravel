package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"reviewBoard","member"})
public class ReviewReply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rrno;
    private String content;

    /* 연관 관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewBoard reviewBoard;

    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;
}
