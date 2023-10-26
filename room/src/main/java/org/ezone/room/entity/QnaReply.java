package org.ezone.room.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","qnaBoard"}) //FK지정 - 외래키로 설정될 엔티티 테이블 이름을 exclude 속성 지정해줌
public class QnaReply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;


    @ManyToOne(fetch = FetchType.LAZY) //지연로딩 설정
    @JoinColumn(name = "bno")
    private QnaBoard qnaBoard; //연관관계 지정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id") // 조인할 필드
    private Member member; // id
}
