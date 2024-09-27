package com.javateam.healthyFoodProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javateam.healthyFoodProject.domain.MemberVO;
import com.javateam.healthyFoodProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("member")
@Slf4j
public class MemberJoinRestController {
	
	@Autowired
	public MemberService memberService;
	
	@PostMapping("joinProc2")
	public String joinProc2(@ModelAttribute("memberDTO") MemberVO objMemberVO, Model model) {
		
		log.info("회원가입처리: {}", objMemberVO);
		String strMsg  = "";				// 저장 성공,실패 메시지
		String strPath = "";				// 처리 후 이동 경로
		
		if(memberService.insertMember(objMemberVO) == true) {
			strMsg  = "회원가입에 성공하셨습니다.";
			strPath = "/login";
		} else {
			strMsg  = "회원가입에 실패하였습니다.";
			strPath = "/joinDemo";
		}
		
		model.addAttribute("msg", strMsg);
		model.addAttribute("path", strPath);
		
		return "/member/result";
	}
	
	@PostMapping("joinProcAjax")
	public ResponseEntity<Boolean> joinProcAjax(@RequestBody MemberVO objMemberVO) {
		
		log.info("회원가입처리(AJAX): {}", objMemberVO);
		ResponseEntity<Boolean> responseEntity = null;
		
		try {
			
			boolean result = memberService.insertMember(objMemberVO);
			log.info("회원 가입 성공 여부: {}", result);
			
			if(result == true) {
				responseEntity = new ResponseEntity<>(result, HttpStatus.OK);			// 회원가입 성공
			} else {
				responseEntity = new ResponseEntity<>(result, HttpStatus.NO_CONTENT);	// 회원가입 실패
			}
		} catch (Exception ex) {
			log.error("[MemberJoinRestController][joinProcAjax] Exception: {}", ex);
			
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	}

}
