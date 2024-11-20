package com.javateam.healthyFoodProject.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class DeleteMemberServiceTest {

	@Autowired
	MemberService memberService;
	
	// 회원 정보 삭제
	@Transactional
	@Rollback(false)
	@Test
	void testDeleteMember() {
		assertTrue(memberService.deleteMember("abcd2222"));
	
	}
	
	// 존재하지 않는 회원정보를 삭제할려고 할 때
	@Transactional
	@Rollback
	@Test
	void testDeleteMemberAbsent() {
		
		assertFalse(memberService.deleteMember("spring12345"));
	}

}
