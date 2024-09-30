package com.javateam.healthyFoodProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javateam.healthyFoodProject.domain.CustomUser;
import com.javateam.healthyFoodProject.domain.MemberUpdateDTO;
import com.javateam.healthyFoodProject.domain.MemberVO;
import com.javateam.healthyFoodProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberUpdateController {

	@Autowired
	public MemberService memberService;
	public BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/update")
	public String update(Model model) {
		
		log.info("회원정보 수정");
		
		// Spring Security Pricipal(Session) 조회
		Object principal = SecurityContextHolder.getContext()
												.getAuthentication()
												.getPrincipal();
		
		CustomUser customUser = (CustomUser)principal;
		
		log.info("principal : ()", principal);
		log.info("id : {}", customUser.getUsername());	// 로그인 아이디
		
		String id = customUser.getUsername();
		
		MemberVO memberVO = memberService.selectMemberById(id);
		
		if(memberVO == null) {
			// 회원정보가 없는 경우, 에러 처리
			model.addAttribute("errorMsg", "회원 정보가 존재하지 않습니다.");
			return "/error/error";
			
		} else {
			
			MemberUpdateDTO memberUpdateDTO = new MemberUpdateDTO(memberVO);
			model.addAttribute("memberUpdateDTO", memberUpdateDTO);
		}
		
		return "member/update";
	}
	
	@PostMapping("/updateProc") 
	public String updateProc(@ModelAttribute("memberUpdateDTO") MemberUpdateDTO memberUpdateDTO, RedirectAttributes redirectAttributes) {
		
		log.info("회원정보 수정 처리: {}", memberUpdateDTO);
		
		// 신규 비밀번호가 공백이 아니면 비밀번호 변경
		// 공백이면 비밀번호 변경 안 함
		if (memberUpdateDTO.getPasswordUpdate().trim().equals("") != true) {
			
			// 비밀번호 암호화
			bCryptPasswordEncoder = new BCryptPasswordEncoder();
			memberUpdateDTO.setPw(bCryptPasswordEncoder.encode(memberUpdateDTO.getPasswordUpdate()));
		}
		log.info("[updateProc] 암호화 이후 : {}", memberUpdateDTO);
		
		String msg = "";
		String movePath = "";
		
		boolean blRetVal = memberService.updateMember(memberUpdateDTO);
		
		if(blRetVal == true) {
			
			msg = "회원정보를 수정했습니다.";
			movePath = "redirect:/member/view";
			
		} else {
			
			msg = "회원정보 수정에 실패했습니다.";
			movePath = "redirect:/member/update";
		}
		log.info("result : {}", msg);
		redirectAttributes.addAttribute("msg", msg);
		
		return movePath;
	}
}
