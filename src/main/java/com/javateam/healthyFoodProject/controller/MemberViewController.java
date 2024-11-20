package com.javateam.healthyFoodProject.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javateam.healthyFoodProject.domain.CustomUser;
import com.javateam.healthyFoodProject.domain.MemberVO;
import com.javateam.healthyFoodProject.domain.SessionUser;
import com.javateam.healthyFoodProject.domain.SocialUser;
import com.javateam.healthyFoodProject.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("member")
@Slf4j
public class MemberViewController {

	@Autowired
	public MemberService memberService;

	// 자체 로그인 회원정보 조회
	@GetMapping("/view")
	public String view(Model model) {

		// Spring Security Principal(Session) 조회
		Object principal = SecurityContextHolder.getContext()
												.getAuthentication()
												.getPrincipal();

		CustomUser customUser = (CustomUser)principal;
		log.info("[MemberViewController][principal] : {}", principal);
		log.info("[MemberViewController][id] : {}", customUser.getUsername()); // 로그인 아이디

		String id = customUser.getUsername();

		MemberVO memberVO = memberService.selectMemberById(id);

		if (memberVO == null) {
			// 에러 처리
			model.addAttribute("errorMsg", "회원 정보가 존재하지 않습니다.");
			return "/error";

		} else {
			model.addAttribute("memberDTO", memberVO);
		}

		return "/member/view";
	}

	// 소셜 로그인 회원정보 조회
	@GetMapping("/viewSocial")
	public String viewSocial(Model model, HttpSession httpSession) {

		log.info("[SessionUser]: {}", httpSession.getAttribute("socialUser"));
		SessionUser sessionUser = (SessionUser)httpSession.getAttribute("socialUser");

		SocialUser socialUser = memberService.selectSocialUser(sessionUser.getEmail(), sessionUser.getAuthVendor());
		log.info("[socialUser]: {}", socialUser);

		String birthyear = new SimpleDateFormat("yyyy년 MM월 dd일").format(Date.valueOf(socialUser.getBirthyear()));
		socialUser.setBirthyear(birthyear); // 2000-06-02 >> 2000년 06월 02일

		model.addAttribute("socialMember", socialUser);

		return "/member/viewSocial";
	}
}
