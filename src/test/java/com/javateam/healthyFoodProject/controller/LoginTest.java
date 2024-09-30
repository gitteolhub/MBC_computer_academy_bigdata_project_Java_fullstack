package com.javateam.healthyFoodProject.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Slf4j
public class LoginTest {

	@Autowired
	private WebApplicationContext webApplicationcontext;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private AuthController authController;
	
	private MockMvc mockMvc;
	
	private String strId;
	private String strPw;
	
	@BeforeEach
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(authController)					// 인증 컨트롤러를 세팅
								 .apply(springSecurity(springSecurityFilterChain))	// springSecurity 적용
								 .build();
	}
	
	@DisplayName("로그인 테스트: 아이디,비밀번호 일치할 경우(정상 가입된 회원정보)")
	@Test
	public void testAuthIdPw() throws Exception {
		
		strId = "abcd1111";
		strPw = "#Abcd1234";
		
		mockMvc.perform(formLogin("/login")						 // 로그인 URL로 post 요청
					.user("userid", strId)						 // 회원 아이디 설정
					.password("password", strPw))				 // 회원 비밀번호 설정
			   .andExpect(authenticated().withUsername(strId))	 // 인증 성공 시 아이디 확인
			   .andDo(print());									 // 요청과 응답을 출력
	}
	
	@DisplayName("로그인 테스트: 아이디가 일치하지만, 비밀번호가 불일치할 경우(정상 가입된 회원정보)")
	@Test
	public void testAuthId() throws Exception {
		
		strId = "abcd1234";
		strPw = "#Abcd12345";
		
		MvcResult mvcResult = mockMvc.perform(formLogin("/login")
										.user("userid", strId)
										.password("password", strPw))
									 .andExpect(unauthenticated())				// 인증 실패 확인
									 .andExpect(redirectedUrl("/loginError"))	// 로그인 실패 시 redirected URL 확인
									 .andDo(print())
									 .andReturn();								// 결과 반환
		
		// 세션에서 예외 메서지를 가져옴
		String sessionMsg = mvcResult.getRequest()
									 .getSession()
									 .getAttribute("SPRING_SECURITY_LAST_EXCEPTION")
									 .toString();
		
		// 기대하는 메시지를 세션 메시지에 표함되어 있는지 확인
		assertThat("비밀번호가 일치하지 않습니다.").isSubstringOf(sessionMsg);
	}

	@DisplayName("로그인 테스트 : 아이디도 없고 비밀번호 불일치할 경우(DB에 등록되지 않은 회원정보)")
	@Test
	public void testAuth() throws Exception {
		
		strId = "abcd12345";
		strPw = "Abcd1111!";
		
		MvcResult mvcResult = mockMvc.perform(formLogin("/login")
										.user("userid", strId)
										.password("password", strPw))
									 .andExpect(unauthenticated())				// 인증 실패 확인
									 .andExpect(redirectedUrl("/loginError"))	// 로그인 실패 시 redirected URL 확인
									 .andDo(print())
									 .andReturn();								// 결과 반환
		
		String sessionMsg = mvcResult.getRequest()
									 .getSession()
									 .getAttribute("SPRING_SECURITY_LAST_EXCEPTION")
									 .toString();
		
		assertThat("회원 아이디가 없습니다.").isSubstringOf(sessionMsg);
	}

}
