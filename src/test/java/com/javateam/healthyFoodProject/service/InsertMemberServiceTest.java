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
					   .id("abcd5555")
					   .pw("#Abcd5555")
					   .name("이민주")
					   .gender("여자")

					   .email("abcd5555@abcd.com")
					   .phone("010-5555-5555")
					   .birthday(Date.valueOf("1997-04-13"))
					   .build();
	}

	@Test
	void testInsertMember() {
		assertTrue(memberService.insertMember(memberVO));
	}

}
