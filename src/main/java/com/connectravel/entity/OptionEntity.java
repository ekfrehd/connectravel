package com.connectravel.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "option")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ono;

    private String optionName;  // 옵션 이름

}
