package com.javateam.healthyFoodProject.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiCaptchaNkeyService {
	
	// API 호출하여 응답을 받아오는 메서드
	public String get(String apiUrl, Map<String, String> requestHeaders) {
		
		HttpURLConnection con = connect(apiUrl);
		
		try {
			con.setRequestMethod("GET");
			for(Map.Entry<String, String> header: requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}
			
			int responseCode = con.getResponseCode();
			log.info("[responseCode]: {}", responseCode);
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				return readBody(con.getInputStream());
			
			} else {
				return readBody(con.getErrorStream());
			}
		} catch (IOException ex) {
			throw new RuntimeException("API 요청과 응답 실패", ex);
			
		} finally {
			con.disconnect();
		}
	}
	
	// URL에 연결을 생성하는 메서드
	public HttpURLConnection connect(String apiUrl) {
		
		try {
			// URL 객체 생성
			URL url = new URL(apiUrl);
			return (HttpURLConnection)url.openConnection();
			
		} catch (MalformedURLException ex) {
			throw new RuntimeException("API URL이 잘못되었습니다.: " + apiUrl, ex);
			
		} catch (IOException ex) {
			throw new RuntimeException("연결이 실패했습니다.: " + apiUrl, ex);
		}
	}
	
	// InputStream에서 응답 본문을 읽어 문자열로 반환하는 메서드
	public String readBody(InputStream body) {
		
		InputStreamReader inputStreamReader = new InputStreamReader(body);
		
		try (BufferedReader lineReader   = new BufferedReader(inputStreamReader)) {
			 StringBuilder  responseBody = new StringBuilder();
			
			String strLine;
			
			// InputStream에서 한 줄씩 읽음
			while ((strLine = lineReader.readLine()) != null) {
				    responseBody.append(strLine);
			}
			
			return responseBody.toString();
			
		} catch (IOException ex) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", ex);
		}
	}

}
