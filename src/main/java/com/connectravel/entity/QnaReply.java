package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member", "qnaBoard"})
public class QnaReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;
    private String content;

    /* 연관 관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    private QnaBoard qnaBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /* 도메인 로직 */
    public void changeContent(String content) {
        this.content = content;
    }
}