package com.javateam.healthyFoodProject.domain;

// 회원 정보를 저장하는 클래스
public class Users {

	private String usernum;
	private String userid;
	private String password;
	private int    enabled;

	// public Users() {}

	public Users(String strUsernum, String strUserid, String strPassword, int intEnabled) {
		this.usernum  = strUsernum;
		this.userid   = strUserid;
		this.password = strPassword;
		this.enabled  = intEnabled;
	}

	@Override
	public String toString() {
		return String.format("Users[usernum=%s, userid=%s, password=%s, enabled=%s]", usernum, userid, password, enabled);
	}
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

	public String getPassword() {
		return password;
	}
	public void setPassword(String strPassword) {
		this.password = strPassword;
	}

	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int intEnabled) {
		this.enabled = intEnabled;
	}


}
