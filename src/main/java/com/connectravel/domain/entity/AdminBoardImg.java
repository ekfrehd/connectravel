package com.connectravel.domain.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "adminBoard")
public class AdminBoardImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // delete 옵션을 cascade로 설정하는 것! room이 삭제되면 img는 쓰레기가 되기 때문에 지워줘야 됨
    private AdminBoard adminBoard;

    @Column(length = 200)
    private String imgFile;

}