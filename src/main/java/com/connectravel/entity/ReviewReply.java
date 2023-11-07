package org.ezone.room.entity;

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

    private String text;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩 설정
    private ReviewBoard reviewBoard; //연관관계 지정

    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;
}
