package com.connectravel.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"reviewBoard","member"})
public class ReviewReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rrno;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewBoard reviewBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member; // id
}