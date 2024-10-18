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
import com.javateam.healthyFoodProject.repository.ChosenFoodMenuDAO;
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

	@Autowired
	JsonService jsonService;

	@Autowired
	ChosenFoodMenuDAO chosenFoodMenuDAO;


	private final SocialUserDAO socialUserDAO;
	private final HttpSession   httpSession;
	private final SocialUserMybatisDAO socialUserMybatisDAO;


	@SuppressWarnings({"rawtypes", "unchecked"})  // 경고 무시
	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException{

		log.info("[oAuth2UserRequest]:{}", oAuth2UserRequest);

		OAuth2UserService delegate   = new DefaultOAuth2UserService();
		OAuth2User        oAuth2User = delegate.loadUser(oAuth2UserRequest);

		// OAuth 인증 정보 추출
		String registrationId        = oAuth2UserRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

		// oAuthAttributes 객체 생성
		OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

		log.info("[oAuthAttributes]:{}", oAuthAttributes);

		SocialUser socialUser = saveOrUpdate(oAuthAttributes);
		socialUser.setAuthVendor(oAuthAttributes.getAuthVendor());

		log.info("[CustomOAuth2UserService][loadUser][socialUser]: {}", socialUser);

		// social(구글 & 네이버) 인증 session
		httpSession.setAttribute("socialUser", new SessionUser(socialUser));

		return new DefaultOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority(socialUser.getRoleKey())),
				oAuthAttributes.getAttributes(),
				oAuthAttributes.getNameAttributeKey());
	}

	private SocialUser saveOrUpdate(OAuthAttributes oAuthAttributes) {

		log.info("[saveOrUpdate],[oAuthAttributes]:{}",oAuthAttributes);

		SocialUser socialUser;

		// naver의 경우
		if(oAuthAttributes.getAuthVendor().equals("naver")==true) {

			socialUser = socialUserDAO.findByEmail(oAuthAttributes.getEmail())
					.map(entity -> entity.update(oAuthAttributes.getName(),
							oAuthAttributes.getGender(),
							oAuthAttributes.getBirthyear(),
							oAuthAttributes.getAuthVendor(),
							oAuthAttributes.getFoodmenu()))
					.orElse(oAuthAttributes.toEntity());

		} else {
			// google의 경우
			log.info("[google Email]: {}",oAuthAttributes.getEmail());

			// 회원정보 부재시(없을때) >> 회원정보 추가
			if(socialUserDAO.findByEmail(oAuthAttributes.getEmail()).isEmpty() == true) {

				log.info("회원정보 없을때");
				oAuthAttributes.setGender("없음");
				oAuthAttributes.setBirthyear("없음");
				oAuthAttributes.setFoodmenu("없음");


				socialUser = socialUserDAO.findByEmail(oAuthAttributes.getEmail())
						.map(entity -> entity.update(oAuthAttributes.getName(),
								oAuthAttributes.getGender(),
								oAuthAttributes.getBirthyear(),
								oAuthAttributes.getAuthVendor(),
								oAuthAttributes.getFoodmenu()))
						.orElse(oAuthAttributes.toEntity());
			} else {
				// 회원정보 있을때
				log.info("회원정보 있을때");
				socialUser = socialUserDAO.findByEmail(oAuthAttributes.getEmail()).get();
			}

			log.info("[socialUser]: {}",socialUser);

		}

		log.info("[saveOrUpdate2]:{}", socialUser);

		try {
			// 회원정보 존재하지 않은면 생성
			if (socialUser.getId() == null) {

				log.info("회원정보 존재하지 않을때");

				//TODO FilePath 지정 필요
				// 회원가입시 초기 식단 추가
				String foodMenuFilePath = "";
				String foodMenu = jsonService.readFoodMenuJson(foodMenuFilePath);
				socialUser.setFoodmenu(foodMenu);   // foodmenu 초기값 설정(null 방지)
				socialUserMybatisDAO.insertSocialUser(socialUser);
				log.info("[socialUser]: {}", socialUser);

				// 가입된 회원정보 불러오기
				socialUser = socialUserMybatisDAO.selectSocialUserByEmailAndAuthVendor(socialUser.getEmail(), socialUser.getAuthVendor());
				log.info("[socialUser2]: {}", socialUser);

				// 회원 아이디 chosenFoodMenu에 추가
				String id = socialUser.getId().toString();
				log.info("[insertIdChosenFoodMenu][id]: {}", id);
				chosenFoodMenuDAO.insertIdChosenFoodMenu(id);

				socialUser = socialUserDAO.findByEmail(socialUser.getEmail()).get();
			}

		} catch(Exception ex){
			log.error("social 회원정보 저장 오류: {}", ex);
		}

		log.info("[saveOrUpdate3]");

		return socialUser;
	}

}
