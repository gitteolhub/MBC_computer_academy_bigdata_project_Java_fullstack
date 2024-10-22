package com.javateam.healthyFoodProject.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.healthyFoodProject.domain.SocialUser;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class UpdateSocialUserServiceTest {

	@Autowired
	MemberService memberService;

	SocialUser socialUser;

	@Autowired
	CustomOAuth2UserService customOAuth2UserService;

	@Test
	void testUpdateSocialUser() {

		log.info("[testUpdateSocialUser]");

		socialUser = new SocialUser();
		socialUser.setAuthVendor("naver");
		socialUser.setBirthyear("2000");
		socialUser.setGender("F");
		socialUser.setEmail("tgdcom7@naver.com");
		socialUser.setName("진소원");

		log.info("[socialUser]: {}", socialUser);
		assertTrue(memberService.updateSocialUser(socialUser));
	}


	// social 회원 별로 바뀔 식단 업데이트
	@Transactional
	@Rollback(false)
	@Test
	public void testUpdateFoodMenuBySocialUser() throws IOException {
		int id = 35;

		boolean blRetVal = customOAuth2UserService.updateFoodMenuBySocialUser(id);

		assertTrue(blRetVal);


	}
}
