package org.ezone.room.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "QnaBoard")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "writer") //FK지정(One쪽의 테이블에서는 외래키 연결시킬 자신의 엔티티의 컬럼명을 exclude 지정)
public class QnaBoard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

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
