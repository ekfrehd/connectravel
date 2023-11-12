package com.connectravel.entity;

import com.connectravel.constant.ReservationStatus;
import lombok.*;

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
    private String message; // 요청 사항
    private int money;

    @Column(nullable = false)
    private int numberOfGuests; // 예약 인원 수

    private LocalDate startDate; // 입실일
    private LocalDate endDate; // 퇴실일

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    /* 연관 관계 */
    @ManyToOne
    private Room room;

    @ManyToOne
    private Member member;

}

