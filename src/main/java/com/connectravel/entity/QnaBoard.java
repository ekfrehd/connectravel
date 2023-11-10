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
@ToString(exclude = "member")
public class QnaBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qbno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    /* 연관 관계 */
    @ManyToOne(fetch = FetchType.LAZY) //지연 로딩 지정
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "qnaBoard", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<QnaReply> replies = new ArrayList<>();

    /* 도메인 로직 */
    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeContent(String content) {
        this.content = content;
    }

}
