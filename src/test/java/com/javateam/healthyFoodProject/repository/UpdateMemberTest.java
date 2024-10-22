package com.javateam.healthyFoodProject.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.healthyFoodProject.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class UpdateMemberTest {

	@Autowired
	public MemberDAO memberDAO;

	public MemberVO memberVO;

	// DB 연결하지 않고 단위 테스트할 때 memberVO 초기화하는 코드
//	@BeforeEach
//	void setUp() throws Exception {
//		memberVO = MemberVO.builder()
//								.id("abcd1111")
//								.pw("#Abcd1234")
//								.email("abcd1111@abcd.com")
//						   .build();
//	}

	// 비밀번호만 변경(수정)
	@Transactional
	@Rollback(true)
	@Test
	void testUpdateMemberByPw() {

		memberVO = MemberVO.builder()
								.id("abcd12234")
								.pw("#Java1111")
						   .build();

		boolean result = memberDAO.updateMember(memberVO);
		assertTrue(result);

		// 기존 비밀번호와 수정 비밀번호와 동등 비교
		String strUpdatePw = memberDAO.selectMemberById(memberVO.getId()).getPw();
		log.info("[strUpdatePw]:{}", strUpdatePw);
		assertEquals("#Java1111",strUpdatePw);
	}

	@Transactional
	@Rollback(true)
	@Test
	public void testUpdateMemberByPw2() {

		String strUpdatePw = memberDAO.selectMemberById("abcd12234").getPw();
		assertNotEquals("#Abcd1234", strUpdatePw);
	}

	@Transactional
	@Rollback(true)
	@Test
	void testUpdateMemberByEmail() {

		memberVO = MemberVO.builder()
								.id("abcd12234")
								.email("abcd1111@abcd.com")
						   .build();

		boolean result = memberDAO.updateMember(memberVO);
		assertTrue(result);

		// 기존 이메일과 수정 이메일 동등 비교
		String strUpdateEmail = memberDAO.selectMemberById("abcd12234").getEmail();
		assertEquals("abcd1111@abcd.com", strUpdateEmail);
	}

	@Transactional
	@Rollback(true)
	@Test
	void testUpdateMemberByPhone() {

		memberVO = MemberVO.builder()
								.id("abcd12234")
								.phone("010-1111-3333")
						   .build();

		boolean result = memberDAO.updateMember(memberVO);
		assertTrue(result);

		String strUpdatePhone = memberDAO.selectMemberById("abcd12234").getPhone();
		assertEquals("010-1111-3333", strUpdatePhone);
	}
}
