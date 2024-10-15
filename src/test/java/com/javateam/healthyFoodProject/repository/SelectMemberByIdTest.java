package com.javateam.healthyFoodProject.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.healthyFoodProject.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@Slf4j
class SelectMemberByIdTest {
	
	@Autowired
	public MemberDAO memberDAO;
	
	// 아이디와 회원 번호가 같은지 테스트
	@Test
	void testSelectMemberByIdNum() {
		String strTestId="abcd2222";
		
		MemberVO objMemberVO = memberDAO.selectMemberById(strTestId);
		assertEquals("21", objMemberVO.getNum());  // ""안에 조회할 회원번호 입력
	}

	// 아이디와 회원명이 같은지 테스트
	@Test
	void testSelectMemberByIdName() {
		String strTestId="abcd1111";
		
		MemberVO objMemberVO = memberDAO.selectMemberById(strTestId);
		log.info("[objMemberVO]: " + objMemberVO);
		
		assertEquals("홍길동", objMemberVO.getName());
	}

	// 아이디와 비밀번호가 같은지 테스트
	@Test
	void testSelectMemberByIdPw() {
		String strTestId="abcd1111";
		
		MemberVO objMemberVO = memberDAO.selectMemberById(strTestId);
		assertEquals("#Abcd1234", objMemberVO.getPw());
	}
	
	// 아이디와 이메일이 같은지 테스트
	@Test
	void testSelectMemberByIdEmail() {
		String strTestId="abcd1111";
		
		MemberVO objMemberVO = memberDAO.selectMemberById(strTestId);
		assertEquals("abcd1111@abcd.com", objMemberVO.getEmail());
	}
}
