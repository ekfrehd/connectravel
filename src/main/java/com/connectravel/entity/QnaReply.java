package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString (exclude = {"member", "qnaBoard"}) //FK지정 - 외래키로 설정될 엔티티 테이블 이름을 exclude 속성 지정해줌
public class QnaReply extends BaseEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long rno; // 댓글 번호

    private String text; // 댓글 내용

    @ManyToOne (fetch = FetchType.LAZY) //지연로딩 설정
    @JoinColumn (name = "bno")
    private QnaBoard qnaBoard; // 연관관계 지정

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "id") // 조인할 필드
    private Member member; // 연관관계 지정

    public void changeText (String text) {
        this.text = text;
    }
}