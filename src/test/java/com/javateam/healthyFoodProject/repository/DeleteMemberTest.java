package com.javateam.healthyFoodProject.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class DeleteMemberTest {
	
	@Autowired
	public MemberDAO memberDAO;

	// 회원(abcd2222)의 role 삭제 점검
	@Transactional
	@Rollback(true)
	@Test
	void testDeleteRoles() {
		assertTrue(memberDAO.deleteRoles("abcd2222"));
	}
	
	//회원정보 삭제
	@Transactional
	@Rollback(true)
	@Test
	void testDeleteMemberById() {
		assertTrue(memberDAO.deleteRoles("abcd2222"));
		assertTrue(memberDAO.deleteMemberById("abcd2222"));
	}
}
