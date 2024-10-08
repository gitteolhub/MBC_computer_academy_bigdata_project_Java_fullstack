package com.javateam.healthyFoodProject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class DeleteCaptchaImageTest {

	@Autowired
	public DeleteCaptchaImageService deleteCaptchaImageService;
	
	@Test
	void deleteImagetest() {
		deleteCaptchaImageService.deleteCaptchaImage();
	}

}
