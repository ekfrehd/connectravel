package com.connectravel.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TourBoard extends BaseEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long tbno; // 번호

    @Column (length = 100, nullable = false)
    private String title; // 제목

    @Column (length = 2000, nullable = false)
    private String content; // 내용

    private String region; // 지역

    private String category; // 카테고리

    private double grade; // 평점

    private int reviewCount; // 리뷰수

    @Column (nullable = false)
    private int postal; // 우편번호

    @Column (nullable = false)
    private String address; // 주소

    /*@OneToMany (mappedBy = "TourBoard",//BoardImage의 Board 변수
            cascade = {CascadeType.ALL},//영속성 전이 작업을 활성화
            fetch = FetchType.LAZY)//지연 로딩
    @Builder.Default//빈 HashSet으로 초기화
    @BatchSize(size = 20)//지정된 수만큼 조회할 때 한번에 in 조건 사용
    //Board 엔티티와 연관된 BoardImage 엔티티의 집합
    private Set<TourBoardImg> imageSet = new HashSet<>();*/

    public void changeTitle (String title) {
        this.title = title;
    }
    public void changeContent (String content) { this.content = content;}
    public void changeRegion (String region) { this.region = region;}
    public void changeCategory (String category) { this.category = category;}
    public void changeAddress (String address) { this.address = address;}
    public void changePostal (Integer postal) { this.postal = postal;}
    public void setGrade (double grade) { // 평점 변경
        this.grade = grade;
    }
    public void setReviewCount (int reviewCount) { // 리뷰 수 변경
        this.reviewCount = reviewCount;
    }

    /*public void addImage (Long ino, String fileName) {//Board 엔티티에 이미지를 추가
        //BoardImage 엔티티를 생성하고, UUID와 파일 이름을 설정한 다음
        //현재 Board 엔티티와 연결, 다음 이미지 세트(imageSet)에 새 이미지를 추가
        TourBoardImg tourBoardImg = TourBoardImg.builder ()
                .ino(ino)
                .imgFile(fileName)
                .tourBoard(this)
                .ord (imageSet.size ())
                .build ();
        imageSet.add (tourBoardImg);
    }

    public void clearImages () {//Board 엔티티와 연결된 모든 이미지를 제거하는 데 사용
        //imageSet의 각 BoardImage 엔티티에 대해 changeBoard(null)을 호출, 해당 이미지의 board 속성을 제거
        imageSet.forEach (tourBoardImg -> tourBoardImg.changeBoard (null));
        this.imageSet.clear ();//imageSet을 비움
    }*/
}

