package com.javateam.healthyFoodProject.domain;

import java.sql.Date;
import java.util.Map;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MemberVO {
	
	private String num;
	private String id;
	private String pw;
	private String name;
	private String gender;
	
	private String email;
	private String phone;
	private Date   birthday;
	private Date   joindate;
	private int    enabled;
	

	@Override
	public boolean equals(Object obj) {
		
		// 같은 객체인지 비교
		if (this == obj)
			return true;
		
		// 비교 대상이 null인지 확인
		if (obj == null)
			return false;
		
		// 클래스 타입이 같은지 확인
		if (getClass() != obj.getClass())
			return false;
		
		MemberVO other = (MemberVO) obj;
		return Objects.equals(num,    other.num)   && Objects.equals(id,       other.id)
			&& Objects.equals(pw,     other.pw)    && Objects.equals(name,     other.name)
			&& Objects.equals(gender, other.gender)&& Objects.equals(email,    other.email)
			&& Objects.equals(phone,  other.phone) && Objects.equals(birthday, other.birthday);
	}

	@Override
	public int hashCode() {
		return Objects.hash(num,   id,    pw,      name, gender,
							email, phone, birthday);
	}
}
