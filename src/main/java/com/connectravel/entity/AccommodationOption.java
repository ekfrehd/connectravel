package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter @Setter
@ToString(exclude = "accommodation")
public class AccommodationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ano", nullable = false)
    private Accommodation accommodation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ono", nullable = false)
    private Option option;
}
