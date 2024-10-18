package com.javateam.healthyFoodProject.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class MemberRestControllerTest {
	
	public MockMvc mockMvc;
	
	@Autowired
	public WebApplicationContext webApplicationContext;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		}
	
	
	// 존재하지 않는 아이디로 변경
	// 기댓값 >> 204 : NO Content (중복 안 됨 => 사용가능)
	@Test
	void testHasFldAbsent() throws Exception {

		mockMvc.perform(get("/member/hasFld/ID/abcd4455")).andExpect(status().isNoContent())
														  .andExpect(content().string("false"))
														  .andDo(print());
	}
	
	// 존재하는 아이디로 변경
	// 기댓값 >> 200 : OK (중복 => 사용불가능) 
	@Test
	void testHasFldPresent() throws Exception {
		
		mockMvc.perform(get("/member/hasFld/ID/abcd2222")).andExpect(status().isOk())
														  .andExpect(content().string("true"))
														  .andDo(print());
	}
	
	// 기존 자신의 이메일로 변경
	// 기댓값 >> 204 : NO Content (사용가능)
	@Test
	void testHasFldForUpdateMyEmail() throws Exception {
		
		mockMvc.perform(get("/member/hasFldForUpdate/abcd2222/EMAIL/abcd3333@abcd.com")).andExpect(status().isNoContent())
		  												     							.andExpect(content().string("false"))
		  												     							.andDo(print());
		
	}

	// 다른 회원이 사용 중인 이메일로 변경
	// 기댓값 >> 200 : OK (중복 => 사용불가능)
	@Test
	void testHasFldForUpdateEmailPresent() throws Exception {
		
		mockMvc.perform(get("/member/hasFldForUpdate/abcd2222/EMAIL/abcd1234@Abcd.com")).andExpect(status().isOk())
		  												     							.andExpect(content().string("true"))
		  												     							.andDo(print());
		
	}
	
	// 사용 중이지 않는 이메일로 변경
	// 기댓값 >> 204 : NO Content (중복 안 됨 => 사용가능)
	@Test
	void testHasFldForUpdateEmailAbsent() throws Exception {
		
		mockMvc.perform(get("/member/hasFldForUpdate/abcd2222/EMAIL/ABCD9988@abcd.com")).andExpect(status().isNoContent())
		  												     							.andExpect(content().string("false"))
		  												     							.andDo(print());
		
	}
}
