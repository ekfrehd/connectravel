package com.connectravel.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "room_img")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "room_id")
public class RoomImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE) // room이 삭제되면 img도 필요없기 때문에 delete 속성으로 같이 삭제해줌
    @JoinColumn(name = "rno")
    private RoomEntity room;

    @Column(length = 200)
    private String imgFile;

}
