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
public class ReservationEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rvno; //예약 번호

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "rno",nullable = false)
    private RoomEntity room;

    @Column(length = 200)
    private String message; //요청사항

    private LocalDate startDate; //예약 시작
    private LocalDate endDate; //예약 종료

    private int money;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id",nullable = false)
    private Member member;

    @Builder.Default
    private boolean state = true;
}

