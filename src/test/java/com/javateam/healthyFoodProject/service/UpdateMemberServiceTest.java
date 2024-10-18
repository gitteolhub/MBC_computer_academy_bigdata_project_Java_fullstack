package com.javateam.healthyFoodProject.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.healthyFoodProject.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class UpdateMemberServiceTest {

	@Autowired
	MemberService memberService;

	MemberVO memberVO;

	// 비밀번호만 변경(수정)
	@Test
	void testUpdatePw() {

		log.info("[testUpdatePw]");

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String pw = bCryptPasswordEncoder.encode("#Abcd1234");

		memberVO = MemberVO.builder()
								.id("abcd2222")
								.pw(pw)
						   .build();

		assertTrue(memberService.updateMember(memberVO));
	}

	// 존재하지 않는 회원정보를 수정하려고 할 때 제대로 롤백되는 지 테스트
	@Transactional
	@Rollback(true)
	@Test
	void testUpdateMemberAbsent() {

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String pw = bCryptPasswordEncoder.encode("#Abcd1234");

		memberVO =  MemberVO.builder()
								.id("abcabcd1111")
				   		        .pw(pw)
				   		    .build();

	assertFalse(memberService.updateMember(memberVO));
	}

	// 사용자별로 바뀔 식단 업데이트
	@Transactional
	@Rollback(false)
	@Test
	public void testUpdateFoodMenuByUser() {
		String id = "abcd3333";

		boolean blRetVal = memberService.updateFoodMenuByUser(id);

		assertTrue(blRetVal);
	}
}
