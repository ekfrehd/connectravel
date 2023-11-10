package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "member")
public class AdminBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 30, nullable = false)
    private String category;

    /* 연관 관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    //grade, photo, bad 기능 추가 필요

    /* 도메인 로직 */
    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeContent(String content) {
        this.content = content;
    }


}
