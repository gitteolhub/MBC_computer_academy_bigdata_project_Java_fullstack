package com.javateam.healthyFoodProject.domain;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class MemberDTO {
	
	private String num;
	private String id;
	private String pw;
	private String name;
	private String gender;
	
	private String email;
	private String phone;
	private Date   birthday;
	private Date   joindate; // 회원 가입일
	private int    enabled;  // 회원 활성화 여부
	
	// DTO >> MultiValueMap
	public static MultiValueMap<String, String > toMap(MemberDTO memberDTO)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		Field[] fields = memberDTO.getClass().getDeclaredFields();
		
		for (Field field: fields) {
			
			if (field.getName().equals("log") == false) {
				Method method = memberDTO.getClass().getDeclaredMethod("get" + StringUtils.capitalize(field.getName()));
				
				if (field.getName().equals("birthday") || field.getName().equals("joindate")) {
					map.put(field.getName(), Arrays.asList(new SimpleDateFormat("yyyy-MM-dd").format(method.invoke(memberDTO))));
					
				} else {
					map.put(field.getName(), Arrays.asList(method.invoke(memberDTO).toString()));
				}
			}
		}
		return map;
	}
	
	public MemberDTO(Map<String, Object> requestMap) {
		
		Set<String> set = requestMap.keySet();
		Iterator<String> iterator = set.iterator();
		Field field;
		
		while (iterator.hasNext()) {
			
			String fldName = iterator.next();
			
			try {
				try {
					field = this.getClass().getDeclaredField(fldName);
					field.setAccessible(true);
					
					if (!fldName.equals("birthday") || !fldName.equals("joindate")) {
						field.set(this, requestMap.get(fldName));
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
