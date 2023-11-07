package org.ezone.room.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "reviewBoard")
public class ReviewBoardImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino;

    @ManyToOne
    private ReviewBoard reviewBoard;

    @Column(length = 200)
    private String imgFile;

}
