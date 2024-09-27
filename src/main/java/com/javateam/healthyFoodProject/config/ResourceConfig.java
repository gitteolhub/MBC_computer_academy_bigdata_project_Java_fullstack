package com.javateam.healthyFoodProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {
	
	
	// 자원 핸들러를 추가하는 메서드
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		// */bootstrap/** 경로로 들어오는 요청을 처리하는 위한 자원 핸들러 추가
		// "classpath:/META-INF/resource/webjars/bootstrap/" 위치에서 자원을 찾도록 설정
		registry.addResourceHandler("/bootstrap/**")
				.addResourceLocations("classpath:/META-INF/resource/webjars/bootstrap/");
		
		registry.addResourceHandler("/axios/**")
				.addResourceLocations("classpath:/META-INF/resource/webjars/axios/");
	}

}
