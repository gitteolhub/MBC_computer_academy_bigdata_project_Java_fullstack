package com.javateam.healthyFoodProject.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
		memberVO = MemberVO.builder()
								.id("abcd2222")
								.pw("#abcd1234")
						   .build();
		
		assertTrue(memberService.updateMember(memberVO));
	}

	// 존재하지 않는 회원정보를 수정하려고 할 때 제대로 롤백되는 지 테스트
	@Transactional
	@Rollback(true)
	@Test
	void testUpdateMemberAbsent() {
		
		memberVO =  MemberVO.builder()
								.id("abcabcd1111")
				   		        .pw("#Java3333")
				   		    .build();
		
	assertFalse(memberService.updateMember(memberVO));
	}
}
