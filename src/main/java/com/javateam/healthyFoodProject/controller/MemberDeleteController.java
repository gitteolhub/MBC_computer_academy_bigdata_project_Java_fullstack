package com.javateam.healthyFoodProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javateam.healthyFoodProject.domain.SessionUser;
import com.javateam.healthyFoodProject.domain.SocialUser;
import com.javateam.healthyFoodProject.repository.SocialUserMybatisDAO;
import com.javateam.healthyFoodProject.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MemberDeleteController {

	@Autowired
	private MemberService memberService;

	@Autowired
	SocialUserMybatisDAO socialUserMybatisDAO;

	@GetMapping("/member/delete")
	public String showDeletePage() {
		return "member/delete";
	}

	// 자체 회원 탈퇴 처리
	@PostMapping("/member/delete")
	public String deleteMember(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

		log.info("회원 탈퇴 처리");

		if(id == null || id.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("msg", "회원 정보를 찾을 수 없습니다.");
			return "redirect:/member/delete";
		}

		try {
			boolean blRetVal = memberService.deleteMember(id);
			log.info("회원 탈퇴 성공 여부: {}", blRetVal);

			if(blRetVal) {
				// 로그아웃 처리
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if(authentication != null) {
					new SecurityContextLogoutHandler().logout(request, response, authentication);
				}

				redirectAttributes.addFlashAttribute("msg", "회원 탈퇴가 완료 되었습니다.");
			} else {
				redirectAttributes.addFlashAttribute("msg", "회원 탈퇴가 실패했습니다.");

			}
		} catch(Exception ex) {
			log.error("[MemberDeleteController][deleteMember] Exception: {}", ex);

		}
		// 탈퇴 결과 페이지
		return "redirect:/member/deleteResult";
	}

	// social 회원 탈퇴 처리
	@PostMapping("/social/delete")
	public String deletSocialUser(@RequestParam("email") String email, @RequestParam("authVendor") String authVendor, RedirectAttributes redirectAttributes) {

		log.info("social회원 탈퇴 처리");

		// social 사용자를 email, authVendor 조회하여 삭제
		SocialUser socialUser = socialUserMybatisDAO.selectSocialUserByEmailAndAuthVendor(email, authVendor);
		if (socialUser == null ) {
			redirectAttributes.addFlashAttribute("msg", "social 회원 정보를 찾을 수 없습니다.");
			return "redirect:/social/delete";
		}

		try {
			// social 회원 탈퇴
			boolean blRetVal = memberService.deleteSocialUser(socialUser);
			log.info("social회원 탈퇴 성공 여부: {}", blRetVal);

			if(blRetVal == true) {
				redirectAttributes.addFlashAttribute("msg", "social 회원 탈퇴가 완료 되었습니다.");
			} else {
				redirectAttributes.addFlashAttribute("msg", "social 회원 탈퇴가 실패했습니다.");
			}

		} catch(Exception ex) {
			log.error("[MemberDeleteController][deletSocialUser] Exception: {}", ex);
		}

		// 탈퇴 후 로그인 페이지로 이동
		return "redirect:/social/deleteResult";
	}

	// social 회원 탈퇴 처리
	@GetMapping("/social/deleteResult")
	public String socialDeleteResult(HttpSession httpSession) {

		String movePath = "";

		// 로그아웃 처리(구글, 네이버 구분)
		SessionUser sessionUser = (SessionUser)httpSession.getAttribute("socialUser");

		if (sessionUser.getAuthVendor().equals("google")) {

			movePath = "redirect:/logoutProc";

		} else { // 네이버

			movePath = "/member/socialDeleteResult";
		}

		// 로그인 페이지로 이동
		return movePath;
	}

}
