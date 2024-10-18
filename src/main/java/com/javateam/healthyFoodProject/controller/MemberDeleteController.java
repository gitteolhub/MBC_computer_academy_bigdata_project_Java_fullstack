package com.javateam.healthyFoodProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javateam.healthyFoodProject.service.MemberService;

@Controller
public class MemberDeleteController {

	@Autowired
	private MemberService memberService;

	@GetMapping("/member/delete")	// 임의로 정함(나중에 수정)
	public String showDeletePage() {
		return "member/delete";
	}

	// 회원 탈퇴 처리
	@PostMapping("/member/delete")
	public String deleteMember(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {
		if(id == null || id.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("msg", "유효하지 않은 아이디입니다.");
			return "redirect:/member/delete";
		}

		boolean blRetVal = memberService.deleteMember(id);

		if(blRetVal) {
			redirectAttributes.addFlashAttribute("msg", "회원 정보가 삭제되었습니다");
		} else {
			redirectAttributes.addFlashAttribute("msg", "회원 정보를 찾을 수 없습니다.");
		}

		return "redirect:/member/deleteResult";
	}

}
