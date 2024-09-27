package com.javateam.healthyFoodProject.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class SelectMemberByFldTest {

	@Autowired
	MemberDAO memberDAO;
	
	@Test
	void testSelectMemberByFldId() {
		Map <String, Object> map = memberDAO.selectMemberByFld("ID", "abcd1111");
		
		assertThat("홍길동", equalTo(map.get("NAME")));
	}
	
	@Test
	void testSelectMembersByFldEmail() {
		
		Map <String, Object> map = memberDAO.selectMemberByFld("Email", "abcd1111@abcd.com");
		
		assertThat("홍길동", equalTo(map.get("NAME")));
	}

	@Test
	void testSelectMembersByFldPhone() {
		
		Map <String, Object> map = memberDAO.selectMemberByFld("PHONE", "010-1111-3333");
		
		assertThat("홍길동", equalTo(map.get("NAME")));
	}
	
}
