package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@ToString(exclude = {"accommodation", "option"})
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
