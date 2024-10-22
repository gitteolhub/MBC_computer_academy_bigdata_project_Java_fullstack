package com.javateam.healthyFoodProject.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class ValidCaptchaServiceTest {

	@Value("${naver.captcha.clientId}")
	public String clientId; // 애플리케이션 클라이언트 아이디값";

	@Value("${naver.captcha.clientSecret}")
	public String clientSecret; // 애플리케이션 클라이언트 시크릿값";

	@Autowired
	public ApiCaptchaNkeyService apiCaptchaNKeyService;

	@Test
	public void test() {

		log.info("[checkCaptcha]");

		log.info("[clientId]: {}", clientId);
		log.info("[clientSecret]: {}", clientSecret);

		String key = "발급된 키를 입력합니다.";
		String captcha = "captcha 문자"; // 발행된 그림의 captcha 문자를 입력합니다.

		String code = "1"; // 키 발급시 0,  캡차 이미지 비교시 1로 세팅
        String apiURL = "https://openapi.naver.com/v1/captcha/nkey?code=" + code
        			  + "&key=" + key + "&value=" + captcha;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String responseBody = apiCaptchaNKeyService.get(apiURL, requestHeaders);

        log.info("responseBody : {}", responseBody);

	}

}
