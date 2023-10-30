package org.ezone.room.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "member") //FK지정(One쪽의 테이블에서는 외래키 연결시킬 자신의 엔티티의 컬럼명을 exclude 지정)
public class ReviewBoard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rbno;

    private double grade;

    @Column(length = 1500, nullable = false)
    private String content;

    @OneToMany(mappedBy = "reviewBoard", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReviewReply> replies = new ArrayList<>();

    @ManyToOne
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Accommodation accommodation;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    public void changeContent(String content){
        this.content = content;
    }


} //class
