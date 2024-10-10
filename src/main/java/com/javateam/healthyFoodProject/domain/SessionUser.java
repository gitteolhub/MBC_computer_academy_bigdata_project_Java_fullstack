package com.javateam.healthyFoodProject.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// session에서 회원 정보를 저장하기 위한 클래스
@ToString
@Getter
@Setter
public class SessionUser implements Serializable{
	private static final long serialVersionUID = 1L;

	private String name;
	private String email;
	private String gender;
	private String birthyear;
	private String authVendor;

	public SessionUser(SocialUser socialUser) {
		this.name       = socialUser.getName();
		this.email      = socialUser.getEmail();
		this.gender     = socialUser.getGender();
		this.birthyear  = socialUser.getBirthyear();
		this.authVendor = socialUser.getAuthVendor();
	}
}
