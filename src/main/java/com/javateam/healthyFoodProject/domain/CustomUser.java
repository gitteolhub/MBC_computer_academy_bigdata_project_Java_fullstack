package com.javateam.healthyFoodProject.domain;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CustomUser implements UserDetails{

	private static final long serialVersionUID = 1L;
	private String usernum;
	private String username;
	private String password;

	private List<Role> authorities;					// 회원 권한 목록
	private boolean accountNonExpired     = true;	// 계정 만료 여부
	private boolean accountNonLocked      = true;	// 겨정 잠금 여부
	private boolean credentialsNonExpired = true;	// 자격 증명 만료 여부
	private boolean enabled               = true;	// 계정 활성화 여부

	public CustomUser(Users users) {
		this.usernum  = users.getUsernum();
		this.username = users.getUserid();
		this.password = users.getPassword();
		this.enabled  = users.getEnabled() == 1 ? true : false;
	}

	public CustomUser(String strUsernum, String strUserid, String strPassword, boolean blEnabled) {
		this.usernum  = strUsernum;
		this.username = strUserid;
		this.password = strPassword;
		this.enabled  = blEnabled;

	}
}
