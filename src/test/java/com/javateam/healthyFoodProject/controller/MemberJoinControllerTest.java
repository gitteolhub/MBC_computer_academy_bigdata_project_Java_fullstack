package com.javateam.healthyFoodProject.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javateam.healthyFoodProject.domain.MemberVO;
import com.javateam.healthyFoodProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class MemberJoinControllerTest {

	@Autowired
	public WebApplicationContext webApplicationContext;

	@Autowired
	public MockMvc mockMvc;

	@Autowired
	public MemberService memberService;
	public MemberVO memberVO;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		memberVO = MemberVO.builder().id("abcd5555")
								  	 .pw("#Abcd5555")
								  	 .name("이민주")
								  	 .gender("여자")

								  	 .email("abcd5555@abcd.com")
								  	 .phone("010-5555-5555")
								  	 .birthday(Date.valueOf("1997-04-13"))
								  	 .build();
	}

//	// 후처리: 테스트시 가입된 회원삭제
//	@AfterEach
//	void tearDown() throws Exception {
//		memberService.deleteMember("abcd4455");
//	}

	@Test
	void testJoinProc() {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "";

		try {
			json = objectMapper.writeValueAsString(memberVO);

		} catch (JsonProcessingException ex) {
			log.error("json Exception: {}", ex);
			ex.printStackTrace();
		}

		try {
			mockMvc.perform(post("/member/joinProc").contentType(MediaType.APPLICATION_JSON)
													.content(json)
													.accept(MediaType.APPLICATION_JSON))
				   .andExpect(status().isOk())
				   .andExpect(model().attribute("msg","회원가입에 성공하셨습니다."))
				   .andExpect(view().name("/member/result"));

		} catch (Exception ex) {
			log.error("[MemberJoinController] Exception: {}", ex);
			ex.printStackTrace();
		}
	}


}
