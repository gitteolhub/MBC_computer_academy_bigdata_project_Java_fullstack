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
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@SequenceGenerator(
		name = "SOCIAL_USER_SEQ_GENERATOR",
		sequenceName = "SOCIAL_USER_SEQ",
		initialValue = 1,
		allocationSize = 1)
@Table(name="SOCIAL_USER")
public class SocialUser extends BaseTimeEntity {
	
	@Id
	@Column(nullable = false, precision = 10, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "SOCIAL_USER_SEQ_GENERATOR")
	private BigDecimal id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String gender;
	
	@Column(nullable = false)
	private Integer birthyear;
	
	@Column(name="auth-vendor")
	private String authVendor;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SocialRole socialRole;
	
	@Builder
	public SocialUser(String strName, String strEmail, String strGender, Integer intBirthyear, SocialRole socialRole, String strAuthVendor) {
		this.name = strName;
		this.email = strEmail;
		this.gender = strGender;
		this.birthyear = intBirthyear;
		this.socialRole = socialRole;
		this.authVendor = strAuthVendor;
	}
	
	public SocialUser update(String strName, String strGender, Integer intBirthyear, String strAuthVendor) {
		this.name = strName;
		this.gender = strGender;
		this.birthyear = intBirthyear;
		this.authVendor = strAuthVendor;
		
		return this;
	}
	
	public String getRoleKey() {
		return this.socialRole.getKey();
	}


}