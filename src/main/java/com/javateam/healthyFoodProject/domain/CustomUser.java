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

	private List<Role> authorities;
	private boolean accountNonExpired     = true;
	private boolean accountNonLocked      = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled               = true;
	
	
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
