package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "member") //FK지정(One쪽의 테이블에서는 외래키 연결시킬 자신의 엔티티의 컬럼명을 exclude 지정)
public class QnaBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno; // 게시글 번호

    @Column(length = 100, nullable = false)
    private String title; //게시글 제목

    @Column(length = 1500, nullable = false)
    private String content; // 게시글 내용

    @ManyToOne(fetch = FetchType.LAZY) //지연 로딩 지정
    private Member member; // id

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    //grade, photo, bad 기능 추가 필요
} //class
