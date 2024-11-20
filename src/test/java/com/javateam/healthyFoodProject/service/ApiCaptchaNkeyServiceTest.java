package com.javateam.healthyFoodProject.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ApiCaptchaNkeyServiceTest {

	@Autowired
	public ApiCaptchaNkeyService apiCaptchaNkeyService;

	@Test
	void test() {

		String clientId     = "YOUR_CLIENT_ID";
		String clientSecret = "YOUR_CLIENT_SECRET";

		// 키 발급시 0, 캡차 이미지 비교시 1로 세팅
		String code = "0";
		String apiURL = "https://openapi.naver.com/v1/captcha/nkey?code=" + code;

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", clientId);
		requestHeaders.put("X-Naver-Client-Secret", clientSecret);


		String responseBody = apiCaptchaNkeyService.get(apiURL, requestHeaders);

		log.info("responseBody: {}", responseBody);
	}

}
