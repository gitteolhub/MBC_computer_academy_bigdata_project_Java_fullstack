package com.javateam.healthyFoodProject.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javateam.healthyFoodProject.service.ApiCaptchaImageService;
import com.javateam.healthyFoodProject.service.ApiCaptchaNkeyService;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CaptchaRestController {
	
	@Value("${naver.captcha.clientId}")
	String clientId; // 애플리케이션 클라이언트 아이디값

	@Value("${naver.captcha.clientSecret}")
	String clientSecret; // 애플리케이션 클라이언트 시크릿값

	@Autowired
	ApiCaptchaImageService apiCaptchaImageService;

	@Autowired
	ApiCaptchaNkeyService apiCaptchaNKeyService;

	@Autowired
	ServletContext servletContext;

	// captcha 이미지 갱신(refresh)
	@GetMapping("refreshImage")
	public ResponseEntity<String> refreshImage() {
		
		log.info("[clientId]: {}", clientId);
		log.info("[clientSecret]: {}", clientSecret);
		
		String responseBody = "";
		
		Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String code    = "0"; // 키 발급시 0,  캡차 이미지 비교시 1로 세팅
        String apiURL  = "https://openapi.naver.com/v1/captcha/nkey?code=" + code;
        String keyJson = apiCaptchaNKeyService.get(apiURL, requestHeaders);
        
        ObjectMapper ObjectMapper = new ObjectMapper();
        String key = "";
        
        try {
        	key = ObjectMapper.readValue(keyJson, Map.class).get("key").toString();
        	log.info("[key(result)]: {}", key);
        	
        } catch (IOException ex) {
        	log.error("[JSON parsing error]");
        	ex.printStackTrace();
        	
        }
        
        apiURL = "https://openapi.naver.com/v1/captcha/ncaptcha.bin?key=" + key;
        String filenameOrMsg = apiCaptchaImageService.get(apiURL,requestHeaders);

        log.info("[filenameOrMsg(메시지)]: {}", filenameOrMsg);
        
        Map<String, String> map = new HashMap<>();
    	map.put("captchaImage", filenameOrMsg);
		map.put("key", key);
		
		try {
			responseBody = ObjectMapper.writeValueAsString(map);
			
		} catch (IOException ex) {
			log.error("[JSON 생성 에러]");
			ex.printStackTrace();
		}
		
		log.info("[responseBody]: {}", responseBody);
		
		return new ResponseEntity<String>(responseBody, HttpStatus.OK);
	}
	
	// captcha 이미지 비교 점검(check)
	@GetMapping("checkCaptcha")
	public ResponseEntity<String> rest(@RequestParam("captchaVal") String captchaVal,
									   @RequestParam("key") String key) {

		log.info("[checkCaptcha]");

		log.info("[clientId]: {}", clientId);
		log.info("[clientSecret]: {}", clientSecret);

		log.info("[key]: {}", key);
		log.info("[captchaVal]: {}", captchaVal);
		
		// 키 발급시 0, 캡차 이미지 비교시 1로 세팅
		String code = "1";
		String apiURL = "https://openapi.naver.com/v1/captcha/nkey?code=" + code
  			  + "&key=" + key + "&value=" + captchaVal;

  		Map<String, String> requestHeaders = new HashMap<>();
  		requestHeaders.put("X-Naver-Client-Id", clientId);
  		requestHeaders.put("X-Naver-Client-Secret", clientSecret);

  		String responseBody = apiCaptchaNKeyService.get(apiURL, requestHeaders);

  		log.info("[responseBody]: {}", responseBody);
  		
  		return new ResponseEntity<String>(responseBody, HttpStatus.OK);
	}	
}
