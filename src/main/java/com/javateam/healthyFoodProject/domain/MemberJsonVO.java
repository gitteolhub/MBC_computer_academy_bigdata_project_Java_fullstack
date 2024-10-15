package com.javateam.healthyFoodProject.domain;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@Builder
@AllArgsConstructor
@ToString
public class MemberJsonVO {
	// social 회원정보
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String gender;

	private String birthyear;
	private String authVendor;
	private String createdDate;
	private String modifiedDate;


	// 자체 회원정보 (비밀번호 제외)
	private String num;
	private String id;
	private String phone;
	private Date   birthday;
	private Date   joindate;

	private int    enabled;
	private String foodmenu;


	public static MemberJsonVO toEntity(MemberVO memberVO) {

		return MemberJsonVO.builder()
						   .num(memberVO.getNum())
						   .id(memberVO.getId())
						   .name(memberVO.getName())
						   .gender(memberVO.getGender())
						   .email(memberVO.getEmail())
						   .phone(memberVO.getPhone())
						   .birthday(memberVO.getBirthday())
						   .joindate(memberVO.getJoindate())
						   .enabled(memberVO.getEnabled())
						   .build();
	}


	public static MemberJsonVO toEntity(SocialUser socialUser) {
		log.info("[SocialUser]: {}",socialUser);

		return MemberJsonVO.builder()
						   .id(socialUser.getId().toPlainString())
						   .name(socialUser.getName())
						   .email(socialUser.getEmail())
						   .gender(socialUser.getGender())
						   .birthyear(socialUser.getBirthyear())
						   .authVendor(socialUser.getAuthVendor())
						   .createdDate(socialUser.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH:mm:ss")))
						   .modifiedDate(socialUser.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH:mm:ss")))
						   .build();
	}
}
