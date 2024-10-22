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
					   .id("abcd9999")
					   .pw("#Abcd9999")
					   .name("우진")
					   .gender("남자")

					   .email("abcd9999@abcd.com")
					   .phone("010-9999-1234")
					   .birthday(Date.valueOf("1999-09-23"))
					   .build();
	}

	@Test
	void testInsertMember() {
		assertTrue(memberService.insertMember(memberVO));
	}

}
