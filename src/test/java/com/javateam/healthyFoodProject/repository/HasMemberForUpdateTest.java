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
	// 회원 기준 메일로 재사용 점검
	@Test
	void testHasMemberForMyEmailUpdate() {
		
		assertFalse(memberDAO.hasMemberForUpdate("abcd1111", "EMAIL", "abcd1111@abcd.com"));
	}
	
	// 신규 메일 사용
	// 다른 회원 이메일과 중복되는 메일 사용 점검
	@Test
	void testHasMemberForDuplicateEmail() {
		
		assertTrue(memberDAO.hasMemberForUpdate("abcd1111", "EMAIL", "abcd2222@abcd.com"));
		
	}
	
	// 신규 메일 사용
	// 다른 회원 이메일과 중복되지 않는 메일 사용 점검
	@Test
	void testHasMemberForEmail() {
		
		assertFalse(memberDAO.hasMemberForUpdate("abcd1111", "EMAIL", "ABCD8787@abcd.com"));
	}

	// 연락처 중복 점검
	// 회원 기준 연락처로 재사용 점검
	@Test
	void testHasMemberForMyPhoneUpdate() {
		
		assertFalse(memberDAO.hasMemberForUpdate("abcd1111", "PHONE", "010-1111-3333"));
	}
	
	// 신규 연락처 사용
	// 다른 회원 연락처와 중복되는 연락처 사용 점검
	@Test
	void testHasMemberForDuplicatePhone() {
		
		assertTrue(memberDAO.hasMemberForUpdate("abcd1111", "PHONE", "010-2222-1234"));
		
	}
	
	// 신규 연락처 사용
	// 다른 회원 연락처와 중복되지 않는 연락처 사용 점검
	@Test
	void testHasMemberForPhone() {
		
		assertFalse(memberDAO.hasMemberForUpdate("abcd1111", "PHONE", "010-1234-5678"));
	}
}
