package com.javateam.healthyFoodProject.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class HasMemberForUpdateTest {

	@Autowired
	public MemberDAO memberDAO;
	
	// 이메일 중복 점검
	// 자신의 기존 메일 입력시 성공하도록 테스트
	@Test
	void testHasMemberForMyEmailUpdate() {
		
		assertFalse(memberDAO.hasMemberForUpdate("abcd2222", "EMAIL", "abcd3333@abcd.com"));
	}
	
	// 다른 회원의 이메일과 중복되는 메일 사용시 실패하도록 테스트
	@Test
	void testHasMemberForDuplicateEmail() {
		
		assertTrue(memberDAO.hasMemberForUpdate("abcd2222", "EMAIL", "abcd1234@Abcd.com"));
		
	}
	
	// 다른 회원의 이메일과 중복되지 않는 메일 사용시 성공하도록 테스트
	@Test
	void testHasMemberForEmail() {
		
		assertFalse(memberDAO.hasMemberForUpdate("abcd2222", "EMAIL", "ABCD8787@abcd.com"));
	}

	// 연락처 중복 점검
	// 자신의 기존 연락처 입력시 성공하도록 테스트
	@Test
	void testHasMemberForMyPhoneUpdate() {
		
		assertFalse(memberDAO.hasMemberForUpdate("abcd2222", "PHONE", "010-2222-1234"));
	}
	
	// 다른 회원의 연락처와 중복되는 연락처 사용시 실패하도록 테스트
	@Test
	void testHasMemberForDuplicatePhone() {
		
		assertTrue(memberDAO.hasMemberForUpdate("abcd2222", "PHONE", "010-5678-1034"));
		
	}
	
	// 다른 회원의 연락처와 중복되지 않는 연락처 사용시 성공하도록 테스트
	@Test
	void testHasMemberForPhone() {
		
		assertFalse(memberDAO.hasMemberForUpdate("abcd2222", "PHONE", "010-1234-5678"));
	}
}
