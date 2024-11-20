package com.javateam.healthyFoodProject.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.javateam.healthyFoodProject.domain.SessionUser;
import com.javateam.healthyFoodProject.domain.SocialLoginUser;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver{

	private final HttpSession httpSession;

	// MethodParameter가 지원되는지 확인하는 메서드
	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {

		boolean isLoginUserAnnotation = methodParameter.getParameterAnnotation(SocialLoginUser.class) != null;
		boolean isUserClass = SessionUser.class.equals(methodParameter.getParameterType());

		return isLoginUserAnnotation && isUserClass;
	}

	// Parameter를 해석하여 실제 객체를 반환하는 메서드
	@Override
	public Object resolveArgument(MethodParameter  methodParameter,  ModelAndViewContainer modelAndViewContainer,
								  NativeWebRequest nativeWebRequest, WebDataBinderFactory  webDataBinderFactory) throws Exception {

		return httpSession.getAttribute("user");
	}
}
