package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "QnaBoard")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "writer") //FK지정(One쪽의 테이블에서는 외래키 연결시킬 자신의 엔티티의 컬럼명을 exclude 지정)
public class QnaBoardEntity extends BaseEntity{

    @Id // PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 일련번호 생성 전략
    private Long bno; //게시글 번호

    @Column(length = 100, nullable = false) // 100자 제한, null 비허용
    private String title; //게시글 제목

    @Column(length = 1500, nullable = false) // 1500자 제한, null 비허용
    private String content; //게시글 내용

    @ManyToOne(fetch = FetchType.LAZY) //지연 로딩 지정
    @JoinColumn(name = "id") // // 조인할 칼럼의 이름
    private Member member; // id

    //grade, photo, bad 기능 추가 필요

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }
} //class
