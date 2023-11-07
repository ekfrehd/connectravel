package com.connectravel.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"room","member"})
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rvno;

    @Column(length = 200)
    private String message; //요청사항

    @Column(nullable = false)
    private int numberOfGuests; // 예약 인원 수

    private LocalDate startDate; //예약 시작
    private LocalDate endDate; //예약 종료
    private int money;

    @ManyToOne
    private Room room;

    @ManyToOne
    private Member member;

    @Builder.Default
    private boolean state = true;
}

