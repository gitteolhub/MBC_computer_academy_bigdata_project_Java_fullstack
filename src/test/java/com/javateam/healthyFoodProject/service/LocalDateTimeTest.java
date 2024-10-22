package com.javateam.healthyFoodProject.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class LocalDateTimeTest {

	@Test
	void test() {
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH:mm:ss");

		log.info("[dateTime]: {}", dateTime);

		String str = dateTime.format(dateTimeFormatter);
		log.info("[str]: {}", str);
	}

}
