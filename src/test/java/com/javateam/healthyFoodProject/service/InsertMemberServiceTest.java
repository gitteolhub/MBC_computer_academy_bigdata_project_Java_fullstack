package com.javateam.healthyFoodProject.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.healthyFoodProject.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@Slf4j
class InsertMemberServiceTest {

	@Autowired
	public MemberService memberService;
	public MemberVO memberVO;

	@BeforeEach
	public void setUp() {

	memberVO = MemberVO.builder()
					   .id("abcd4444")
					   .pw("#Abcd2349")
					   .name("우빈")
					   .gender("남자")

					   .email("abcd4444@abcd.com")
					   .phone("010-4444-1234")
					   .birthday(Date.valueOf("2000-02-25"))
					   .build();
	}

	@Test
	void testInsertMember() {
		assertTrue(memberService.insertMember(memberVO));
	}

}
