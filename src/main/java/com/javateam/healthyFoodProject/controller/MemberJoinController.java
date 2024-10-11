package com.javateam.healthyFoodProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javateam.healthyFoodProject.domain.MemberVO;
import com.javateam.healthyFoodProject.service.MemberService;
import com.javateam.healthyFoodProject.domain.ResultMessage;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("member")
@Slf4j
public class MemberJoinController {

	@Autowired
	public MemberService memberService;

	@PostMapping("joinProcDemo")
	public String joinProcDemo(@ModelAttribute MemberVO objMemberVO, Model model) {

		log.info("회원가입 처리: {}", objMemberVO);
		String msg = "";	 	 // 저장 성공,실패 메시지
		String movePath= "";	 // 처리 후 이동 경로


		if (memberService.insertMember(objMemberVO) == true) {
			msg  = "회원가입에 성공하셨습니다.";
			movePath = "/login";
		} else {
			msg  = "회원가입에 실패하였습니다.";
			movePath = "/joinDemo";
		}

		model.addAttribute("msg",  msg);
		model.addAttribute("path", movePath);

		return "/member/result";
	}

	@PostMapping("joinProc")
	public String joinProc(@RequestBody MemberVO objMemberVO, Model model) {

		ResultMessage resultMessage = new ResultMessage();

		log.info("회원가입 처리: {}", objMemberVO);
		String msg = ""; 	// 저장 성공,실패 메시지

		if (memberService.insertMember(objMemberVO) == true) {
			msg = "회원가입에 성공하셨습니다.";
		} else {
			msg = "회원가입에 실패하였습니다.";
		}

		model.addAttribute("msg", msg);
		return "/member/result"; // result.html (thymeleaf)
	}

}
