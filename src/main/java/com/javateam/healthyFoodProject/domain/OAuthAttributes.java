package com.javateam.healthyFoodProject.domain;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@Slf4j
public class OAuthAttributes {

	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String gender;

	private String birthyear;
	private String authVendor;

	@Builder
	public OAuthAttributes(Map<String, Object> attribute,    String strNameAttributeKey, String strName, String strEmail, String strGender,
			String strBirthyear, String strAuthVendor ) {
		this.attributes       = attribute;
		this.nameAttributeKey = strNameAttributeKey;
		this.name             = strName;
		this.email            = strEmail;
		this.gender           = strGender;

		this.birthyear  = strBirthyear;
		this.authVendor = strAuthVendor;
	}

	public static OAuthAttributes of(String strRegistrationId, String strUserNameAttributeName, Map<String, Object> attributes) {

		OAuthAttributes result = null;

		if("naver".equals(strRegistrationId)) {

			log.info("[naver]");
			result = ofNaver("id", attributes);

		} else if("google".equals(strRegistrationId)) {

			log.info("[google]");
			result = ofGoogle(strUserNameAttributeName, attributes);
		}

		log.info("[strRegistrationId]: {} ", strRegistrationId);

		result.setAuthVendor(strRegistrationId);

		return result;
	}

	private static OAuthAttributes ofGoogle (String strUserNameAttributeName, Map<String, Object> attributes) {

		return OAuthAttributes.builder()
				  			  .strName((String) attributes.get("name"))
				  			  .strEmail((String) attributes.get("email"))
//				  			  .strGender((String) attributes.get("gender"))   // 성별 추가
				  			  //.intBirthyear(Integer.parseInt((String)attributes.get("birthyear"))) // 출생년도 추가
				  			  .attribute(attributes)
				  			  .strNameAttributeKey(strUserNameAttributeName)
				  			  .build();
	}

	@SuppressWarnings("unchecked")
	private static OAuthAttributes ofNaver(String strUserNameAttributeName, Map<String, Object> attributes) {

		Map<String, Object> response = (Map<String, Object>) attributes.get("response");

		// 생년월일 추가 naver 생일 : birthyear + "-" + birthday   ex) 2000-01-01
		String birthday = (String)response.get("birthyear") + "-" + (String)response.get("birthday");

		return OAuthAttributes.builder()
							  .strName((String) response.get("name"))
							  .strEmail((String) response.get("email"))
							  .strGender((String) response.get("gender"))
							  .strBirthyear(birthday)
							  .attribute(response)
							  .strNameAttributeKey(strUserNameAttributeName)
							  .build();
	}

	public SocialUser toEntity() {
		return SocialUser.builder()
						 .strName(name)
						 .strEmail(email)
						 .strGender(gender)
						 .strBirthyear(birthyear)
						 .strAuthVendor(authVendor)
						 .role(SocialRole.USER)
						 .build();
	}

}
