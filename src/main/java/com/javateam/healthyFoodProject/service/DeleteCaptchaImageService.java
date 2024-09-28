package com.javateam.healthyFoodProject.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeleteCaptchaImageService {
	
	// 캡차 이미지 파일을 삭제하는 메서드
	public void deleteCaptchaImage() {
		
		Path path = null;
		ClassPathResource classPathResource = new ClassPathResource("/static/captcha/image/");
		
		try {
			path = Paths.get(classPathResource.getURI());
			
		} catch (IOException ex) {
			log.error("경로 error");
			ex.printStackTrace();
		}
		
		try {
			// 지정된 경로의 모든 파일 삭제
			FileUtils.cleanDirectory(new File(path.toString()));
			
		} catch (IOException ex) {
			log.error("captcha 이미지 파일 삭제 오류");
			ex.printStackTrace();
		}
	}

}
