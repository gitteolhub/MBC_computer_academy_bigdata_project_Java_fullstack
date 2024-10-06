package com.javateam.healthyFoodProject.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.javateam.healthyFoodProject.domain.OAuthAttributes;
import com.javateam.healthyFoodProject.domain.SessionUser;
import com.javateam.healthyFoodProject.domain.SocialUser;
import com.javateam.healthyFoodProject.repository.SocialUserDAO;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Getter
@Setter
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	private final SocialUserDAO socialUserDAO;
	private final HttpSession httpSession;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException{
		
		OAuth2UserService delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(oAuth2UserRequest);
		
		String registrationId = oAuth2UserRequest.getClientRegistration()
												 .getRegistrationId();
		
		String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
														.getProviderDetails()
														.getUserInfoEndpoint()
														.getUserNameAttributeName();
		
		OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
		
		SocialUser socialUser = saveOrUpdate(oAuthAttributes);
		socialUser.setAuthVendor(oAuthAttributes.getAuthVendor());
		
		log.info("[CustomOAuth2UserService][loadUser][socialUser]: {}", socialUser);
		
		httpSession.setAttribute("socialUser", new SessionUser(socialUser));
		
		return new DefaultOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority(socialUser.getRoleKey())),
				oAuthAttributes.getAttributes(),
				oAuthAttributes.getNameAttributeKey());
	}
	
	private SocialUser saveOrUpdate(OAuthAttributes oAuthAttributes) {
		
		SocialUser socialUser = socialUserDAO.findByEmail(oAuthAttributes.getEmail())
								.map(entity -> entity.update(oAuthAttributes.getName(), oAuthAttributes.getGender(), oAuthAttributes.getBirthyear(), oAuthAttributes.getAuthVendor()))
								.orElse(oAuthAttributes.toEntity());
		
		return socialUserDAO.save(socialUser);
	}

}
