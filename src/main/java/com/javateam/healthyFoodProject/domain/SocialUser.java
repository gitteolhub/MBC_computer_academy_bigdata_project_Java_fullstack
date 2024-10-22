package com.javateam.healthyFoodProject.domain;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@SequenceGenerator(
		name           = "SOCIAL_USER_SEQ_GENERATOR",
		sequenceName   = "SOCIAL_USER_SEQ",
		initialValue   = 1,
		allocationSize = 1)
@Table(name="SOCIAL_USER")
public class SocialUser extends BaseTimeEntity {	 			// BaseTimeEntity를 상속받아 생성일 및 수정일 정보 포함

	@Id
	@Column(nullable = false, precision = 10, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "SOCIAL_USER_SEQ_GENERATOR")	// 시퀀스를 사용하여 값 자동 생성
	private BigDecimal id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String gender;

	@Column(nullable = false)
	private String birthyear;

	@Column(name="auth_vendor")
	private String authVendor;

	@Column
	private String foodmenu;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SocialRole role;

	// 객체 생성
	@Builder
	public SocialUser(String strName, String strEmail, String strGender, String strBirthyear, SocialRole role,
					  String strAuthVendor, String strFoodmenu) {
		this.name       = strName;
		this.email      = strEmail;
		this.gender     = strGender;
		this.birthyear  = strBirthyear;
		this.role       = role;

		this.authVendor = strAuthVendor;
		this.foodmenu   = strFoodmenu;
	}

	// 회원 정보를 업데이트하는 메서드
	public SocialUser update(String strName, String strGender, String strBirthyear, String strAuthVendor, String strFoodmenu) {
		this.name       = strName;
		this.gender     = strGender;
		this.birthyear  = strBirthyear;
		this.authVendor = strAuthVendor;
		this.foodmenu   = strFoodmenu;

		return this;
	}

	// 역할 키를 반환하는 메서드
	public String getRoleKey() {
		return this.role.getKey();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SocialUser [id=").append(id)  .append(", name=").append(name)          .append(", email=").append(email)
			   .append(", gender=").append(gender)    .append(", birthyear=").append(birthyear).append(", authVendor=").append(authVendor)
			   .append(", foodmenu=").append(foodmenu).append(", role=").append(role)          .append(", getCreatedDate()=").append(getCreatedDate())
			   .append(", getModifiedDate()=").append(getModifiedDate()).append("]");
		return builder.toString();
	}

}
