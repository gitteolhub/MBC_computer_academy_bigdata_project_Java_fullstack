package com.javateam.healthyFoodProject.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;


@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

	// 엔티티가 생성될 때 자동으로 현재 시간을 설정
	@CreatedDate
	private LocalDateTime createdDate;

	// 엔티티가 수정될 때 자동으로 현재 시간을 설정
	@LastModifiedDate
	private LocalDateTime modifiedDate;

}
