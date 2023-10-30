package com.connectravel.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners (AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public abstract class BaseTimeEntity {

    @CreatedDate // 생성일시 저장
    @Column (updatable = false) // DB 수정 불가
    private LocalDateTime regTime; // 등록일

    @LastModifiedDate // 최종 수정일시 저장
    private LocalDateTime modTime; // 수정일
}