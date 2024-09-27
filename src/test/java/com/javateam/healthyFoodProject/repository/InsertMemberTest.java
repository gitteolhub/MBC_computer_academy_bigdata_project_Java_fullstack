package com.javateam.healthyFoodProject.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;


import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.healthyFoodProject.domain.MemberVO;


import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@Slf4j
class InsertMemberTest {

	@Autowired
	public MemberDAO memberDAO;
	public MemberVO  memberVO;
	
	@BeforeEach
	public void setUp() {
	
	memberVO = MemberVO.builder()
					   .id("abcd12234")
					   .pw("#Abcd22223")
					   .name("지원")
					   .gender("여자")
						  
					   .email("abcd1234@Abcd.com")
					   .phone("010-5678-1034")
					   .birthday(Date.valueOf("2000-11-23"))
					   .build();
	}
	@Transactional
	@Rollback(false) //테스트 후 롤백하지 않음
	@Test
	void testInsertMember() {

		log.info("memberVO: {}", memberVO);
		
		boolean blRetVal = memberDAO.insertMember(memberVO);
		
		assertThat(true, equalTo(blRetVal));
	}

}
