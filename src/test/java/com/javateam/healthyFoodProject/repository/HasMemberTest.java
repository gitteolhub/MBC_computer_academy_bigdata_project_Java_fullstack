package com.javateam.healthyFoodProject.repository;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@Slf4j
class HasMemberTest {
	
	@Autowired
	public MemberDAO memberDAO;

	@Transactional(readOnly = true)
	
	// 회원 번호 중 "ABD999"가 없는지 확인
	// 없으면 false
	@Test
	void testHasMemberByNum() {
		assertFalse(memberDAO.hasMemberByFld("NUM", "ABC999"));
	}
	
	
	// 회원 번호 중 "2"가 있는지 확인
	// 있으면 true
	@Test
	void testHasMemberByNumOverlap() {
		assertTrue(memberDAO.hasMemberByFld("NUM", "2"));
	}
	
	// 아이디 중 "javaSpring" 없는지 확인
	// 없으면 false
	@Test
	void testHasMemberById() {
		assertFalse(memberDAO.hasMemberByFld("ID", "javaSpring"));
		
	}
	
	// 아이디 중 "abcd1111"가 있는지 확인
	// 있으면 true
	@Test
	void testHasMemberByIdOverlap() {
		assertTrue(memberDAO.hasMemberByFld("ID","abcd1111"));
	}
	
	// 비밀번호 중 "ASD9999"가 없는지 확인
	// 없으면 false
	@Test
	void testHasMemberByPw() {
		assertFalse(memberDAO.hasMemberByFld("PW","ASD9999"));
	}
	
	// 비밀번호 중 "#Abcd2222"가 있는지 확인
	// 있으면 true
	@Test 
	void testHasMemberByPwOverlap() {
		assertTrue(memberDAO.hasMemberByFld("PW","#Abcd2222"));
	}
	
	// 회원명 중 "이름"이 있는지 확인
	// 회원명은 중복 허용
	@Test
	void testHasMemberByName() {
		assertFalse(memberDAO.hasMemberByFld("NAME", "이름"));
	}
	
	// 회원명 중 "하리보"가 있어도 false
	// 이름 중복 허용(동명이인)
	@Test
	void testHasMemberByNameOverlap() {
		assertFalse(memberDAO.hasMemberByFld("NAME", "하리보"));
	}
	
	// 중복, 중복x >> false
	// 성별 입력 중복 허용("남자", "여자")
	@Test 
	void testHasMemberByGenderOverlap() {
		assertFalse(memberDAO.hasMemberByFld("GENDER", "여자"));
	}
	
	// 이메일 중 "ABCD9999@abcd.com"가 없는지 확인
	// 없으면 false
	@Test
	void testHasMemberByEmail() {
		assertFalse(memberDAO.hasMemberByFld("EMAIL", "ABCD9999@abcd.com"));
	}
	
	// 이메일 중 "abcd1111@abcd.com"가 있는지 확인
	// 있으면 true
	@Test
	void testHasMemberByEmailOverlap() {
		assertTrue(memberDAO.hasMemberByFld("EMAIL", "abcd1111@abcd.com"));
	}
	
	// 폰 번호 중 "011-1234-5678"가 없는지 확인
	// 없으면 false
	@Test
	void testHashMemberByPhone() {
		assertFalse(memberDAO.hasMemberByFld("PHONE", "011-1234-5678"));
	}
	
	// 폰 번호 중 "010-2222-1234"가 있는지 확인
	// 있으면 true
	@Test
	void testHashMemberByPhoneOverlap() {
		assertTrue(memberDAO.hasMemberByFld("PHONE", "010-2222-1234"));
	}
	
}
