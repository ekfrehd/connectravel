package org.ezone.room.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"reviewBoard","member"}) //FK지정 - 외래키로 설정될 엔티티 테이블 이름을 exclude 속성 지정해줌
public class ReviewReply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rrno;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩 설정
    private ReviewBoard reviewBoard; //연관관계 지정

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member; // id
}
