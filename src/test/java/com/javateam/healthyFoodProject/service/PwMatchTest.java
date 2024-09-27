package com.javateam.healthyFoodProject.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class PwMatchTest {
	
	@Test
	void PwMatchtest() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		
		String pw = "#Abcd1234";
		boolean result = bCryptPasswordEncoder.matches(pw, "$2a$10$nwZedgB05kJqU9TB8dDadOVsgBPdKYnV7EgtHgY9DuFUhZYeApAre");
		
		assertTrue(result);
	}



}
