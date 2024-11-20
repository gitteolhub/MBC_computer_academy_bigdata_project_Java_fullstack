package com.javateam.healthyFoodProject.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.healthyFoodProject.domain.SocialUser;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class DeleteSocialMemberTest {

	@Autowired
	public MemberService memberService;

	@Transactional
	@Rollback(false)
	@Test
	void testDelete() {
		log.info("[testDelete]");
		SocialUser socialUser = new SocialUser();
//		socialUser.setId(new BigDecimal(39));
		socialUser.setAuthVendor("naver");
		socialUser.setEmail("tgdcom7@naver.com");
		assertTrue(memberService.deleteSocialUser(socialUser));
	}
}
