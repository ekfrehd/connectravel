package com.connectravel.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ono;

    private String optionName;  // 옵션 이름

    private String optionCategory;

}
