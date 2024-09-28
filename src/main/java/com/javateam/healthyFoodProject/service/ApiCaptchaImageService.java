package com.javateam.healthyFoodProject.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiCaptchaImageService {

	@Autowired
	ServletContext servletContext;
	
	// API 호출하여 이미지 캡차를 가져오는 메서드
	public String get(String apiUrl, Map<String, String> requestHeaders) {
		
		HttpURLConnection con = connect(apiUrl);
		
		try {
			con.setRequestMethod("GET");
			for(Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}
			
			int responseCode = con.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				
				String strImage = getImage(con.getInputStream());
				log.info("200 OK: {}", strImage);
				
				return strImage;
				
			} else {
				return error(con.getErrorStream());
			}
		} catch (IOException ex) {
			throw new RuntimeException("API 요청과 응답 실패", ex);
			
		} finally {
			con.disconnect();
		}
	}
	
	// 주어진 URL에 연결을 생성하는 메서드
	public HttpURLConnection connect(String apiUrl) {
		
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection)url.openConnection();
			
		} catch (MalformedURLException ex) {
			throw new RuntimeException("API URL이 잘못되었습니다.: " + apiUrl, ex);
			
		} catch (IOException ex) {
			throw new RuntimeException("연결이 실패했습니다.: " + apiUrl, ex);
		}
	}
	
	// InputStream에서 이미지를 읽어 파일로 저장하는 메서드
	public String getImage(InputStream inputStream) {
		
		ClassPathResource classPathResource = new ClassPathResource("/static/captcha/image");
		Path path = null;
		
		try {
			path = Paths.get(classPathResource.getURI());
			
		} catch (IOException ex) {
			log.error("경로 검색 에러");
			ex.printStackTrace();
		}
		
		log.info("[ApiCaptchaImageService][path]: {}", path);
		
		int intRead;
		byte[] bytes = new byte[1024];
		
		// 랜덤한 이름으로 파일 생성
		String strFilename = "cap_" + UUID.randomUUID().toString().replaceAll("-", "") 
									+ ".jpg";
		
		File file = new File(path + "/" + strFilename);
		
		try (OutputStream outputStream = new FileOutputStream(file)) {
			
			file.createNewFile();
			
			// inputStream에서 데이터를 읽어 파일로 저장
			while ((intRead = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, intRead);
			}
			
			return strFilename;
			
		} catch (IOException ex) {
			throw new RuntimeException("이미지 캡차 파일 생성에 실패 했습니다.", ex);
		}
	}
	
	// API 응답 에러를 읽어 문자열로 반환하는 메서드
	public String error(InputStream body) {
		
		InputStreamReader inputStreamReader = new InputStreamReader(body);
		
		try (BufferedReader lineReader   = new BufferedReader(inputStreamReader)) {
			StringBuilder   responseBody = new StringBuilder();
			
			String strLine;
			
			// inputStream에서 한 줄씩 읽음
			while ((strLine = lineReader.readLine()) != null) {
				responseBody.append(strLine);
			}
			
			// 최종 오류 메시지 반환
			return responseBody.toString();
			
		} catch (IOException ex) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", ex);
		}
	}
}
