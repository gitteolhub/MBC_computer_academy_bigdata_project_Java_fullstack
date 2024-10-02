package com.javateam.healthyFoodProject.service;

import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.javateam.healthyFoodProject.domain.CustomUser;
import com.javateam.healthyFoodProject.domain.Role;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomProviderService implements AuthenticationProvider, UserDetailsService {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	// 회원아이디로 회원 정보를 로드하는 메서드
	@Override
	public CustomUser loadUserByUsername(String userid) {
		log.info("[loadUserByUsername]");
		
		try {
			return(CustomUser)jdbcTemplate.queryForObject(
					"SELECT NUM as USERNUM, "
						 + " ID as USERNAME, "
						 + " PW as PASSWORD, "
						 + " ENABLED "
						 + "FROM USER_TBL WHERE ID=?",
						 
					new BeanPropertyRowMapper<CustomUser>(CustomUser.class),
					new Object[] {userid});
			
		} catch (EmptyResultDataAccessException ex) {
			log.info("[CustomProviderService][loadUserByUsername] exception");
			
			return null;
		}
	}
	
	private List<Role> loadUserRole(String userid) {
		log.info("[loadUserRole]");
		
		try {
			return(List<Role>)jdbcTemplate.query(
				"SELECT USERID, ROLE "
			   +  "FROM USER_ROLES "
			   + "WHERE USERID=?",
			   
			   new BeanPropertyRowMapper<Role>(Role.class),
			   new Object[] {userid});
			
		} catch (EmptyResultDataAccessException ex) {
			log.info("[CustomProviderService][loadUserRole] exception");
			
			return null;
		}
	}
	
	@Override
	public Authentication authenticate (Authentication authentication) throws AuthenticationException {
		log.info("[authenticate]: {} ",authentication);
		
		String     username = authentication.getName();
		String     password = "";
		CustomUser customUser     = null;
		
		Collection<? extends GrantedAuthority> authorities = null;
		
		try {
			if (username.trim().equals("")) {
				
				throw new InternalAuthenticationServiceException("회원 아이디를 입력하십시오.");
			}
			if (this.loadUserByUsername(username) == null) {
				
				throw new UsernameNotFoundException("회원 아이디가 없습니다.");
			}
			
			customUser = this.loadUserByUsername(username);
			log.info("[사용자현황 (customUser)]: {}", customUser);
			
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			
			password = (String) authentication.getCredentials();
			log.info("[password]: {}", password);
			
			if (passwordEncoder.matches(password, customUser.getPassword()))
				
				log.info("비밀번호가 일치합니다.");
			
			else {
				throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
			}
			
			List<Role> roles = this.loadUserRole(username);
			customUser.setAuthorities(roles);
			
			authorities = customUser.getAuthorities();	
			
		} catch (InternalAuthenticationServiceException ex)  {
			
			log.info("회원 아이디 미입력: ", ex.toString());
			throw new InternalAuthenticationServiceException(ex.getMessage());
			
		} catch(UsernameNotFoundException ex) {
			
			log.info("회원 아이디가 없음: ", ex.toString());
			throw new InternalAuthenticationServiceException(ex.getMessage());
			
		} catch(BadCredentialsException ex) {
			
			log.info("비밀번호가 잘못되었음: ", ex.toString());
			throw new BadCredentialsException(ex.getMessage());
			
		} catch(Exception ex) {
			
			log.info("다른 종류 에러: ", ex.toString());
			ex.printStackTrace();
		}
		
		
		
		log.info("인증절차 끝");
		
		return new UsernamePasswordAuthenticationToken(customUser, password, authorities);
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
}