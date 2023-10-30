package com.connectravel.domain.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "accommodation")
public class AccommodationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aono;

    /* 연관 관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ano", nullable = false)
    private Accommodation accommodation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ono", nullable = false)
    private Option option;

}