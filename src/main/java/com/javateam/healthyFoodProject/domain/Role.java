package com.javateam.healthyFoodProject.domain;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {
	
	private static final long serialVersionUID = 700000000000000L;
	
	private String usernum;
	private String userid;
	private String role;
	
	public String getUsernum() {
		return usernum;
	}
	public void setUsernum(String strUsernum) {
		this.usernum = strUsernum;
	}
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String strUserid) {
		this.userid = strUserid;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String strRole) {
		this.role = strRole;
	}
	
	@Override
	public String getAuthority() {
		return this.role;
	}
	@Override
	public String toString() {
		return "Role [usernum=" + usernum + ",userid= " + userid + ",role=" + role +"]";
	}

}
