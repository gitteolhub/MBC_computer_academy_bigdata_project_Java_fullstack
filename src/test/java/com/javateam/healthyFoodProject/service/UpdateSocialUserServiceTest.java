package com.javateam.healthyFoodProject.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.healthyFoodProject.domain.SocialUser;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class UpdateSocialUserServiceTest {

	@Autowired
	MemberService memberService;

	SocialUser socialUser;

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

}
