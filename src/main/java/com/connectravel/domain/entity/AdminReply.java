package com.connectravel.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "adminBoard")
public class AdminReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bno")
    private AdminBoard adminBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Member member; // id

}