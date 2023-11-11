package com.connectravel.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","qnaBoard"})
public class QnaReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qrno;
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