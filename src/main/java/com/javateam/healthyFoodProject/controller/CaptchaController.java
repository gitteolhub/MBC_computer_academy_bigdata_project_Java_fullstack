package com.javateam.healthyFoodProject.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javateam.healthyFoodProject.service.ApiCaptchaImageService;
import com.javateam.healthyFoodProject.service.ApiCaptchaNkeyService;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CaptchaController {
	@Value("${naver.captcha.clientId}")
	public String clientId; //애플리케이션 클라이언트 아이디값";

	@Value("${naver.captcha.clientSecret}")
	public String clientSecret; //애플리케이션 클라이언트 시크릿값";

	@Autowired
	public ApiCaptchaImageService apiCaptchaImageService;

	@Autowired
	public ApiCaptchaNkeyService apiCaptchaNKeyService;

	@Autowired
	public ServletContext servletContext;

//	@GetMapping
	public String login(Model model) {

		log.info("[CaptchaController].login");

		apiCaptchaImageService = new ApiCaptchaImageService();
		apiCaptchaNKeyService  = new ApiCaptchaNkeyService();

		log.info("[clientId]: {}", clientId);
		log.info("[clientSecret]: {}", clientSecret);

		String code   = "0"; // 키 발급시 0,  캡차 이미지 비교시 1로 세팅
        String apiURL = "https://openapi.naver.com/v1/captcha/nkey?code=" + code;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String keyJson = apiCaptchaNKeyService.get(apiURL, requestHeaders);
        if(keyJson == null) {
        	model.addAttribute("error", "캡차 키를 가져오는데 실패했습니다.");
        }
        log.info("[keyJson]: {}", keyJson);

        ObjectMapper objectMapper = new ObjectMapper();
        String key = "";

        try {

        	key = (String)objectMapper.readValue(keyJson, Map.class).get("key");
            log.info("[key(result)]: {}", key);

		} catch (IOException ex) {
			log.error("[JSON parsing error]");
			ex.printStackTrace();
		}

        apiURL = "https://openapi.naver.com/v1/captcha/ncaptcha.bin?key=" + key;
        String filenameOrMsg = apiCaptchaImageService.get(apiURL,requestHeaders);

        if (filenameOrMsg == null) {
        	model.addAttribute("error", "캡차 이미지를 가져오는데 실패 했습니다.");
        }

        log.info("[filenameOrMsg(메시지)]: {}", filenameOrMsg);

		model.addAttribute("captchaImage", filenameOrMsg);
		model.addAttribute("key", key); // 발급받은 캡차키(key)

		return "loginForm";		// 주소변경
	}

}
