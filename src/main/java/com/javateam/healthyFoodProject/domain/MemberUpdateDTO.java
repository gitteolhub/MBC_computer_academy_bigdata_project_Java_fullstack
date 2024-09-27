package com.javateam.healthyFoodProject.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateDTO extends MemberVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/* 회원 비밀번호(수정) */
	private String passwordUpdate;
	
	/* 회원 비밀번호(확인) */
	private String passwordVerify;
	
	public MemberUpdateDTO(MemberVO objmemberVO) {
		this.setNum(objmemberVO.getNum());
		this.setId(objmemberVO.getId());
		this.setPw(objmemberVO.getPw());
		this.setName(objmemberVO.getName());
		this.setGender(objmemberVO.getGender());
		
		this.setEmail(objmemberVO.getEmail());
		this.setPhone(objmemberVO.getPhone());
		this.setBirthday(objmemberVO.getBirthday());
		this.setJoindate(objmemberVO.getJoindate());
		this.setEnabled(objmemberVO.getEnabled());
	}
	
	@Override
	public String toString() {
		return "MemberUpdateDTO [passwordUpdate=" + passwordUpdate + ", passwordVerify=" + passwordVerify + ", getNum()="     + getNum()
							+ ", getId()="        + getId()        + ", getPw()="        + getPw()        + ", getName()="    + getName()
							+ ", getGender()="    + getGender()    + ", getEmail()="     + getEmail()     + ", getPhone()="   + getPhone()
							+ ", getBirthday()="  + getBirthday()  + ", getJoindate()="  + getJoindate()  + ", getEnabled()=" + getEnabled() 
							+ "]";
							}
	
}
