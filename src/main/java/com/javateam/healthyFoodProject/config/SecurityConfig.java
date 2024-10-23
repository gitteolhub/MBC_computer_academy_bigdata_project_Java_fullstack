package com.javateam.healthyFoodProject.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;

import com.javateam.healthyFoodProject.domain.SocialRole;
import com.javateam.healthyFoodProject.service.CustomOAuth2UserService;
import com.javateam.healthyFoodProject.service.CustomProviderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

//	@Autowired
	private final CustomOAuth2UserService customOAuth2UserService;

	@Autowired
	private CustomProviderService customProviderService;
//	private final UserDetailsService userDetailsService;
	private final DataSource dataSource;

//	// 생성자 주입을 통해 UserDetailsService와 DataSource를 초기화
//	public SecurityConfig(UserDetailsService objUserDetailsService, DataSource objDataSource) {
//		log.info("생성자 주입 wiring");
//		this.dataSource         = objDataSource;
//		this.userDetailsService = objUserDetailsService;
//	}

	// 비밀번호를 안전하게 암호화하기 위해 BCryptPasswordEncoder 빈 생성
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity objHttpSecurity) throws Exception {

		// 사용자 세부 정보를 제공하는 서비스 설정
//		objHttpSecurity.userDetailsService(userDetailsService);

		// custom 인증 제공자 설정
//		objHttpSecurity.authenticationProvider(customProviderService);

		// HTTP 헤더 설정 (10.11)
//		objHttpSecurity.headers(headers -> headers
//					   		.frameOptions(frameOptions -> frameOptions
//					   				.sameOrigin()
//					   		)
//						);


		objHttpSecurity.headers(headersCustomizer -> headersCustomizer
					   .frameOptions(Customizer.withDefaults()).disable());

		// 요청 권한 설정
		objHttpSecurity.authorizeHttpRequests((authorizeHttpRequests) ->
											   authorizeHttpRequests.requestMatchers("/",                    "/demo",                "/resources/**",  "/loginError",      "/join",
													   							 	 "/joinAjax",            "/joinDemo",            "/joinAjaxDemo",  "/member/joinProc", "/member/joinProc2",
													   							 	 "/member/joinProcDemo", "/member/joinProcAjax", "/login/idCheck", "/loginForm",       "/member/hasFld/**",
													   							 	 "/home", "/foodMenu")
											   						.permitAll()
											   						.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")				// ROLE_ADMIN 권한이 필요한 경로
											   						.requestMatchers("/secured/**", "/myPage", "/member/view", "/member/hasFldForUpdate/**", "/member/update",
											   										 "/member/updateProc")
											   						.hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")	// ROLE_USER, ROLE_ADMIN 권한이 필요한 경로
											   						.anyRequest().authenticated());

		// csrf 토큰 미사용
		objHttpSecurity.csrf((csrf) -> csrf.disable());

		// 로그인/ 로그아웃(인증) 처리
		objHttpSecurity.formLogin(formLogin -> formLogin
					   		.loginProcessingUrl("/loginForm")
					   		.loginPage("/loginForm")		// 로그인 이후 주소
					   		.usernameParameter("userid")	// 아이디
					   		.passwordParameter("password")	// 비밀번호
					   		.defaultSuccessUrl("/myPage")	// 로그인 성공시 이동 주소
					   		.failureUrl("/loginError")		// 로그인 에러 처리
					   		.permitAll())

					   .logout((logout) -> logout
							.logoutSuccessUrl("/loginFrom")	// 로그아웃 이후 이동 주소
							.permitAll());

		objHttpSecurity.oauth2Login(oauth2LoginCustomizer -> oauth2LoginCustomizer
							.defaultSuccessUrl("/myPage")
//							.loginProcessingUrl("/login")	// 추가
//					   		.loginPage("/login")			// 추가
							.userInfoEndpoint(userInfoEndpointCustomizer -> userInfoEndpointCustomizer
		  							.userService(customOAuth2UserService)));

		// 예외처리 이용 주소
		objHttpSecurity.exceptionHandling(handler -> handler.accessDeniedPage("/403"));

		// Remember-Me 설정
//		objHttpSecurity.rememberMe((remember) -> remember.key("javateam")							// Remember-Me 키
//														 .userDetailsService(userDetailsService)	// 사용자 세부 정보 서비스 설정
//														 .tokenRepository(getJDBCRepository())		// 토큰 저장소 설정
//														 .tokenValiditySeconds(60 * 60 * 24));		// 토큰 유효 기간 설정 (24시간)

		return objHttpSecurity.build();
	}

	// remember-me 관련 정보 DB 저장
	private PersistentTokenRepository getJDBCRepository() {

		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		repo.setDataSource(dataSource);

		return repo;
	}

	// token 기반 remember-me 서비스
//	@Bean
//	RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
//
//		RememberMeTokenAlgorithm encodingAlgorithm = RememberMeTokenAlgorithm.SHA256; // SHA256알고리즘 사용
//		TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices("javateam", userDetailsService, encodingAlgorithm);
//		rememberMe.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);	//매칭 알고리즘 사용
//
//		return rememberMe;
//	}

	// security URL 열외(제외)
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {

		return (web) -> web.ignoring().requestMatchers("/bootstrap/**", "/css/**", "/js/**", "/axios/**", "/captcha/**", "/webjars/**");
	}
}