package com.javateam.healthyFoodProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javateam.healthyFoodProject.domain.CustomUser;
import com.javateam.healthyFoodProject.domain.MemberVO;
import com.javateam.healthyFoodProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("member")
@Slf4j
public class MemberViewController {

	@Autowired
	public MemberService memberService;

	@GetMapping("/view")
	public String view(Model model) {

		// Spring Security Principal(Session) 조회
		Object principal = SecurityContextHolder.getContext()
												.getAuthentication()
												.getPrincipal();

		CustomUser customUser = (CustomUser)principal;
		log.info("principal : {}", principal);
		log.info("id : {}", customUser.getUsername()); // 로그인 아이디

		String id = customUser.getUsername();

		MemberVO memberVO = memberService.selectMemberById(id);

		if (memberVO == null) {
			// 에러 처리
			model.addAttribute("errorMsg", "회원 정보가 존재하지 않습니다.");
			return "/error/error";

		} else {
			model.addAttribute("memberDTO", memberVO);
		}

		return "/member/view";
	}

}
