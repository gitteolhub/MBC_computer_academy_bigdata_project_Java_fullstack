package com.javateam.healthyFoodProject.domain;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class SessionUser implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String email;
	private Integer birthyear;
	private String authVendor;
	
	public SessionUser(SocialUser socialUser) {
		this.name = socialUser.getName();
		this.email = socialUser.getEmail();
		this.birthyear = socialUser.getBirthyear();
		this.authVendor = socialUser.getAuthVendor();
	}
}
