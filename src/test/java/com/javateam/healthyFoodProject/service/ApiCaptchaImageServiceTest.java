package com.javateam.healthyFoodProject.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ApiCaptchaImageServiceTest {

	@Autowired
	public ApiCaptchaNkeyService apiCaptchaNkeyService;

	@Autowired
	public ApiCaptchaImageService apiCaptchaImageService;

	@Test
	void test() {

		String clientId = "YOUR_CLIENT_ID";
		String clientSecret = "YOUR_CLIENT_SECRET";

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", clientId);
		requestHeaders.put("X-Naver-Client-Secret", clientSecret);

		// 키 발급시 0, 캡차 이미지 비교시 1로 세팅
		String code    = "0";
		String apiURL  = "https://openapi.naver.com/v1/captcha/nkey?code=" + code;
		String keyJson = apiCaptchaNkeyService.get(apiURL, requestHeaders);

		log.info("[keyJson]: {}", keyJson);

		ObjectMapper objectMapper = new ObjectMapper();
		String key = "";

		try{
			key = objectMapper.readValue(keyJson, Map.class).get("key").toString();
			log.info("[key(result)]: {}", key);

		} catch (IOException ex) {

			log.error("[ApiCaptchaImageServiceTest][JSON parsing error]");
			ex.printStackTrace();
		}

		apiURL = "https://openapi.naver.com/v1/captcha/ncaptcha.bin?key=" + key;

		String responseBody = apiCaptchaImageService.get(apiURL, requestHeaders);

		log.info("[responseBody]: {}", responseBody);
	}

}
