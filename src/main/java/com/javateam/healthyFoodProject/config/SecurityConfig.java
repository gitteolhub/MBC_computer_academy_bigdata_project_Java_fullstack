package com.javateam.healthyFoodProject.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

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

	// 비밀번호를 안전하게 암호화하기 위해 BCryptPasswordEncoder 빈 생성
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity objHttpSecurity) throws Exception {

		objHttpSecurity.headers(headersCustomizer -> headersCustomizer
					   .frameOptions(Customizer.withDefaults()).disable());

		// 요청 권한 설정
		objHttpSecurity.authorizeHttpRequests((authorizeHttpRequests) ->
											   authorizeHttpRequests.requestMatchers("/",                 "/resources/**",        "/loginError",          "/join",          "/member/joinProc",
													   								 "/member/joinProc2", "/member/joinProcDemo", "/member/joinProcAjax", "/login/idCheck", "/loginForm",
													   								 "/member/hasFld/**", "/home",                "/captcha",             "/checkCaptcha")
											   						.permitAll()
											   						.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")				// ROLE_ADMIN 권한이 필요한 경로
											   						.requestMatchers("/secured/**",        "/myPage",   "/member/view",      "/member/hasFldForUpdate/**", "/member/update",
											   										 "/member/updateProc", "/foodMenu", "/foodMenu/dislike", "/foodMenu/like",             "/foodMenu/refresh",
											   										 "/foodMenu/view",     "/foodMenu/viewJson")
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
							.logoutSuccessUrl("/loginForm")	// 로그아웃 이후 이동 주소
							.permitAll());

		objHttpSecurity.oauth2Login(oauth2LoginCustomizer -> oauth2LoginCustomizer
							.defaultSuccessUrl("/myPage")
//							.loginProcessingUrl("/login")	// 추가
					   		.loginPage("/loginForm")			// 추가
					   		.failureUrl("/loginForm")
							.userInfoEndpoint(userInfoEndpointCustomizer -> userInfoEndpointCustomizer
		  							.userService(customOAuth2UserService)));

		// 예외처리 이용 주소
		objHttpSecurity.exceptionHandling(handler -> handler.accessDeniedPage("/403"));


		return objHttpSecurity.build();
	}

	// remember-me 관련 정보 DB 저장
	private PersistentTokenRepository getJDBCRepository() {

		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		repo.setDataSource(dataSource);

		return repo;
	}


	// security URL 열외(제외)
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {

		return (web) -> web.ignoring().requestMatchers("/bootstrap/**", "/css/**", "/js/**", "/axios/**", "/captcha/**", "/webjars/**");
	}
}