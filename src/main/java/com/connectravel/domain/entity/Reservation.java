package com.connectravel.domain.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"room","member"})
public class Reservation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rvno; //예약 번호

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room; // 방 번호

    @Column(length = 200)
    private String message; // 요청사항

    private LocalDate startDate; // 예약 시작
    private LocalDate endDate; // 예약 종료

    private int money;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder.Default
    private boolean state = true;
}