package com.javateam.healthyFoodProject.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javateam.healthyFoodProject.domain.MemberVO;
import com.javateam.healthyFoodProject.service.MemberService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("admin")
@Slf4j
public class AdminRestController {
	
	@Autowired
	public MemberService memberService;
	
	// 회원 role 수정
	@GetMapping("/updateRoles/{id}/roleUser/(roleUserYn)/roleAdmin/{roleAdminYn}")
	public ResponseEntity<Boolean> updateRoles(@Parameter(name = "id", required = true)          @PathVariable("id") String strId,
											   @Parameter(name = "roleUserYn",  required = true) @PathVariable("roleUserYn")  boolean blRoleUserYn,
											   @Parameter(name = "roleAdminYn", required = true) @PathVariable("roleAdminYn") boolean blRoleAdminYn) {
		
		log.info("회원 등급(role) 수정 REST(회원 정보 role 수정): {}, {}, {}", strId, blRoleUserYn, blRoleAdminYn);
		
		ResponseEntity<Boolean> responseEntity = null;
		
		try {
			boolean blRetVal = memberService.updateRoles(strId, blRoleUserYn, blRoleAdminYn);
			
			log.info("[AdminRestController][updateRoles] blRetVal: {}", blRetVal);
			
			if (blRetVal == true) {
				// 중복된 아이디가 있음: 성공 코드(200)
				responseEntity = new ResponseEntity<>(blRetVal, HttpStatus.OK);
			} else {
				// 중복된 아이디가 없음: 실패 코드(204)
				responseEntity = new ResponseEntity<>(blRetVal, HttpStatus.NO_CONTENT);
			}
		} catch (Exception ex) {
			log.error("[AdminRestController][updateRoles] error: {}", ex);
			ex.printStackTrace();
			
			// 내부 서버 에러: 실패 코드(417)
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	}
	
	// 회원 활동, 휴먼 변경
	@GetMapping("/changeMemberState/{id}/{enabled}")
	public ResponseEntity<Boolean> changeMemberState(@Parameter(name = "id", required = true) @PathVariable("id") String strId,
													 @Parameter(name = "enabled", required = true) @PathVariable("enabled") int intEnabled) {
		
		log.info("회원 활동, 휴면 계정 처리: {}", strId);
		
		ResponseEntity<Boolean> responseEntity = null;
		
		try {
			boolean blRetVal = memberService.changeEnabled(strId, intEnabled);
			
			log.info("[AdminRestController][changeMemberState] blRetVal: {}", blRetVal);
			
			if (blRetVal == true) {
				// 중복된 아이디가 있음: 성공 코드(200)
				responseEntity = new ResponseEntity<>(blRetVal, HttpStatus.OK);
			} else {
				// 중복된 아이디가 없음: 실패 코드(204)
				responseEntity = new ResponseEntity<>(blRetVal, HttpStatus.NO_CONTENT);
			}
		} catch (Exception ex) {
			log.error("[AdminRestController][changeMemberState] error: {}", ex);
			ex.printStackTrace();
			
			// 내부 서버 에러: 실패 코드(417)
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	// 관리자 >> 회원 정보 수정
	@PostMapping("/updateMemberByAdmin")
	public ResponseEntity<Boolean> updateMember(@Parameter(required = true) @RequestParam Map<String, Object> requestMap) {
		
		log.info("회원 정보 수정 처리(관리자 REST): ");
		
		requestMap.entrySet().forEach(x ->{log.info("인자: {}", x); });
		MemberVO memberVO = new MemberVO(requestMap);
		
		log.info("[AdminRestController]MemberVO: {}", memberVO);
		
		ResponseEntity<Boolean> responseEntity = null;
		
		try {
			boolean blRetVal = memberService.updateMember(memberVO);
			
			log.info("[AdminRestController][updateMember] blRetVal: {}", blRetVal);
			
			if (blRetVal == true) {
				// 중복된 아이디가 있음: 성공 코드(200)
				responseEntity = new ResponseEntity<>(blRetVal, HttpStatus.OK);
			} else {
				// 중복된 아이디가 없음: 실패 코드(204)
				responseEntity = new ResponseEntity<>(blRetVal, HttpStatus.NO_CONTENT);
			}
		} catch (Exception ex) {
			log.error("[AdminRestController][updateMember] error: {}", ex);
			ex.printStackTrace();
			
			// 내부 서버 에러: 실패 코드(417)
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	// 관리자 >> 회원 정보 삭제
	@GetMapping("/deletMemberByAdmin/{id}")
	public ResponseEntity<Boolean> deletMember(@Parameter(name = "id", required = true) @PathVariable(value = "id", required = true) String strId){
		
		log.info("회원 정보 삭제 처리(관리자 REST): {}", strId);
		
		ResponseEntity<Boolean> responseEntity = null;
		
		try {
			boolean blRetVal = memberService.deleteMember(strId);
			
			log.info("[AdminRestController][deletMember] blRetVal: {}", blRetVal);
			
			if (blRetVal == true) {
				// 중복된 아이디가 있음: 성공 코드(200)
				responseEntity = new ResponseEntity<>(blRetVal, HttpStatus.OK);
			} else {
				// 중복된 아이디가 없음: 실패 코드(204)
				responseEntity = new ResponseEntity<>(blRetVal, HttpStatus.NO_CONTENT);
			}
		} catch (Exception ex) {
			log.error("[AdminRestController][deletMember] error: {}", ex);
			ex.printStackTrace();
			
			// 내부 서버 에러: 실패 코드(417)
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
		
	}
}
