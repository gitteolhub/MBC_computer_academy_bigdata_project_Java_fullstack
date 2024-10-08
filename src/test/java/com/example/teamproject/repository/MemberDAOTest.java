package com.example.teamproject.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.teamproject.domain.MemberVO;

@SpringBootTest
class MemberDAOTest {

	@Autowired
	MemberDAO memberDAO;
	
	// 개별 회원 정보를 조회
	@Test
	void testSelectMemberById() {
		
		MemberVO memberVO = memberDAO.selectMemberById("abcd1111");
		assertEquals("홍길동", memberVO.getName());
	}

	@Test
	void testSelectMemberById2() {
		
		MemberVO memberVO = memberDAO.selectMemberById("abcd2222");
		assertEquals("류관순", memberVO.getName());
	}
}
