package com.javateam.healthyFoodProject.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.javateam.healthyFoodProject.repository.SocialUserMybatisDAO;

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
	private final HttpSession   httpSession;
	private final SocialUserMybatisDAO socialUserMybatisDAO;


	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException{

		log.info("[oAuth2UserRequest]:{}", oAuth2UserRequest);

		OAuth2UserService delegate   = new DefaultOAuth2UserService();
		OAuth2User        oAuth2User = delegate.loadUser(oAuth2UserRequest);

		String registrationId        = oAuth2UserRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

		OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

		log.info("[oAuthAttributes]:{}", oAuthAttributes);

		// naver 생일 : birthyear + "-" + birthday   ex) 2000-01-01
		// google :

		// 에러남
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

		log.info("[saveOrUpdate],[oAuthAttributes]:{}",oAuthAttributes);

		SocialUser socialUser;

		if(oAuthAttributes.getAuthVendor().equals("naver")==true) {	// naver의 경우

			socialUser = socialUserDAO.findByEmail(oAuthAttributes.getEmail())
					.map(entity -> entity.update(oAuthAttributes.getName(),
							oAuthAttributes.getGender(),
							oAuthAttributes.getBirthyear(),
							oAuthAttributes.getAuthVendor()))
					.orElse(oAuthAttributes.toEntity());

		} else { // google의 경우

			socialUser = socialUserDAO.findByEmail(oAuthAttributes.getEmail())
					.map(entity -> entity.update(oAuthAttributes.getName(),
							"없음",
							"없음",
							oAuthAttributes.getAuthVendor()))
					.orElse(oAuthAttributes.toEntity());

			socialUser.setGender("없음");
			socialUser.setBirthyear("없음");
		}

		log.info("[saveOrUpdate2]:{}", socialUser);
		// return socialUserDAO.save(socialUser);
		try {

			if (socialUser.getId() == null) { 	// 회원정보 존재하지 않은면 생성
				socialUserMybatisDAO.insertSocialUser(socialUser);
				socialUser = socialUserDAO.findByEmail(socialUser.getEmail()).get();
			}

		} catch(Exception ex){
			log.error("social 회원정보 저장 오류: {}", ex);
		}

		log.info("[saveOrUpdate3]");

		return socialUser;
	}

}
