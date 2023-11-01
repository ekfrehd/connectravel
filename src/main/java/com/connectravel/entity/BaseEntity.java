package com.connectravel.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners (AuditingEntityListener.class)
@MappedSuperclass
@Getter @Setter
public class BaseEntity extends BaseTimeEntity {

    @CreatedBy // 생성자 저장
    @Column (updatable = false) //DB 수정 불가
    private String createdBy; // 생성자

    @LastModifiedBy // 최종 수정자 저장
    private String modifiedBy; //수정자
}