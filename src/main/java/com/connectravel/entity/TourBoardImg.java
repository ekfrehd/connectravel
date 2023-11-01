package com.connectravel.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "tourBoard")

public class TourBoardImg { //implements Comparable<TourBoardImg>
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) //delete 옵션을 cascade로 설정하는것! room이 삭제되면 img는 쓰레기가 되디때문에 지워줘야됨
    private TourBoard tourBoard;

    @Column(length = 200)
    private String imgFile;


    private int ord;

   /* @Override
    public int compareTo (TourBoardImg other) {
        return this.ord - other.ord;
    }//BoardImage의 board 필드를 변경하는 메서드

    public void changeBoard (TourBoard tourBoard) { this.tourBoard = tourBoard; }//이미지들을 정렬하는 메서드*/

}
