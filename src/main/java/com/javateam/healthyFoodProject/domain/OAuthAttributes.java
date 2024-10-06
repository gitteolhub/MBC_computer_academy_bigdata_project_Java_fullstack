package com.javateam.healthyFoodProject.domain;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class OAuthAttributes {
	
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String gender;
	
	private Integer birthyear;
	private String authVendor;
	
	@Builder
	public OAuthAttributes(Map<String, Object> attribute,    String strNameAttributeKey, String strName, String strEmail, String strGender, 
						               Integer intBirthyear, String strAuthVendor ) {
		this.attributes = attribute;
		this.nameAttributeKey = strNameAttributeKey;
		this.name = strName;
		this.email = strEmail;
		this.gender = strGender;
		
		this.birthyear = intBirthyear;
		this.authVendor = strAuthVendor;
	}
	
	public static OAuthAttributes of(String strRegistrationId, String strUserNameAttributeName, Map<String, Object> attributes) {
		
		OAuthAttributes result = null;
		
		if("naver".equals(strRegistrationId)) {
			result = ofNaver("id", attributes);
		} else if("google".equals(strRegistrationId)) {
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
				  .attribute(attributes)
				  .strNameAttributeKey(strUserNameAttributeName)
				  .build();
	}
	
	@SuppressWarnings("unchecked")
	private static OAuthAttributes ofNaver(String strUserNameAttributeName, Map<String, Object> attributes) {
		
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		
		return OAuthAttributes.builder()
							  .strName((String) response.get("name"))
							  .strEmail((String) response.get("email"))
							  .attribute(response)
							  .strNameAttributeKey(strUserNameAttributeName)
							  .build();
	}
	
	public SocialUser toEntity() {
		return SocialUser.builder()
						 .strName(name)
						 .strEmail(email)
						 .strAuthVendor(authVendor)
						 .socialRole(SocialRole.USER)
						 .build();
	}

}
