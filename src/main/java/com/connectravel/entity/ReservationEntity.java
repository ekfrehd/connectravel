package com.connectravel.entity;


import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
//예약 엔티티
public class ReservationEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rvno; //예약 번호

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "rno",nullable = true)
    private RoomEntity room_id; //방 번호

    @Column(length = 200)
    private String message; //요청사항

    private LocalDate StartDate; //예약 시작
    private LocalDate EndDate; //예약 종료

    private int money;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member_id; //맴버 이름

    @Builder.Default
    private boolean state = true;
}

