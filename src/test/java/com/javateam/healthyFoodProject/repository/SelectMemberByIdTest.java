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
	
	// 아이디를 통한 회원번호 조회 테스트
	@Test
	void testSelectMemberByIdNum() {
		String strTestId="abcd2222";
		
		MemberVO objMemberVO = memberDAO.selectMemberById(strTestId);
		assertEquals("21", objMemberVO.getNum());  // ""안에 조회할 회원번호 입력
	}

	// 아이디를 통한 회원명 조회 테스트
	@Test
	void testSelectMemberByIdName() {
		String strTestId="abcd2222";
		
		MemberVO objMemberVO = memberDAO.selectMemberById(strTestId);
		log.info("[objMemberVO]: " + objMemberVO);
		
		assertEquals("남우민", objMemberVO.getName());
	}

	// 아이디를 통한 비밀번호 조회 테스트
	@Test
	void testSelectMemberByIdPw() {
		String strTestId="abcd2222";
		
		MemberVO objMemberVO = memberDAO.selectMemberById(strTestId);
		assertEquals("$2a$10$r1KdA1ZOiRCmHBtf3qk3DuaoxKCnch7kzdlw6cRqV8u8XNBts.uDq", objMemberVO.getPw());
	}
	
	// 아이디를 통한 이메일 조회 테스트
	@Test
	void testSelectMemberByIdEmail() {
		String strTestId="abcd2222";
		
		MemberVO objMemberVO = memberDAO.selectMemberById(strTestId);
		assertEquals("abcd3333@abcd.com", objMemberVO.getEmail());
	}
}
