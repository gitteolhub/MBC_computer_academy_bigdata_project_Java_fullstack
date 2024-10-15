package com.javateam.healthyFoodProject.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class MemberJsonServiceTest {

	@Autowired
	MemberJsonService memberJsonService;

	@Autowired
	MemberService memberService;

	@Test
	void test() {
		memberService.selectAllMembersJson();
	}

	@Test
	void testSaveMemberDataJson() {
		memberJsonService.saveMemberDataJson();
	}

}
