package com.javateam.healthyFoodProject.domain;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
	public boolean equals(Object object) {
		
		// 같은 객체인지 비교
		if (this == object)
			return true;
		
		// 비교 대상이 null인지 확인
		if (object == null)
			return false;
		
		// 클래스 타입이 같은지 확인
		if (getClass() != object.getClass())
			return false;
		
		MemberVO other = (MemberVO) object;
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
	
	public MemberVO(Map<String, Object> requestMap) {
		
		Set<String> set = requestMap.keySet();
		Iterator<String> iterator = set.iterator();
		
		// reflection 정보 활용
		Field field;
		
		while (iterator.hasNext()) {
			
			String fieldName = iterator.next();
			
			try {
				try {
					field = this.getClass().getDeclaredField(fieldName);
					
					// private 필드에 접근할 수 있다록 설정
					field.setAccessible(true);
					
					
					// birthday 와 joindate 필드는 설정하지 않음
					if(!fieldName.equals("birthday") || !fieldName.equals("joindate")) {
						
						// 요청 맵에서 해당 필드의 값을 가져와 필드에 설정
						field.set(this,requestMap.get(fieldName));
					}
				} catch (NoSuchFieldException ex) {
					
					log.info("인자와 필드가 일치하지 않습니다.");
				}
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
	}
}
