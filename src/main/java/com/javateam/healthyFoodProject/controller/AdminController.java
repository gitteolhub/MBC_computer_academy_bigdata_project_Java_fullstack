package com.javateam.healthyFoodProject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.healthyFoodProject.domain.PageVO;
import com.javateam.healthyFoodProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
	
	@Autowired
	public MemberService memberService;
	
	@GetMapping("/")
	public String admin() {
		
		log.info("관리자 화면 admin");
		
		return "redirect:/admin/viewAllWithRoles";
	}
	
	@GetMapping("/viewAllWithRoles")
	public String adminViewWithRoles(@RequestParam(value = "currentPage", defaultValue = "1", required = true) int currentPage,
									 @RequestParam(value = "limit",       defaultValue = "20") int intLimit,
									 Model model) {
		
		log.info("관리자 화면: role 표시");
		List<Map<String, Object>> members = new ArrayList<>();
		
		members = memberService.selectMembersBySearchingAndPaging(null, null, currentPage, intLimit, null, null);
		
		// 총 인원 수
		int listCount = memberService.selectCountAll();
		
		log.info("총 회원수: {}", listCount);
		
		// 총 페이지 수
		int maxPage = PageVO.getMaxPage(listCount, intLimit);
		
		// 현재 페이지에 보여줄 시작 페이지 수
		int startPage = PageVO.getStartPage(currentPage, intLimit);
		
		// 현재 페이지에 보여줄 마지막 페이지 수
		int endPage = startPage + 10;
		if (endPage > maxPage) endPage = maxPage;
		
		PageVO pageVO = new PageVO();
		
		pageVO.setEndPage(endPage);
		pageVO.setListCount(listCount);
		pageVO.setMaxPage(maxPage);
		pageVO.setCurrentPage(currentPage);
		pageVO.setStartPage(startPage);
		
		pageVO.setPreviousPage(pageVO.getCurrentPage() - 1 < 1 ? 1 : pageVO.getCurrentPage() - 1);
		pageVO.setNextPage(pageVO.getCurrentPage() + 1 > pageVO.getEndPage() ? pageVO.getEndPage() : pageVO.getCurrentPage() + 1);
		
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("members", members);
		
		return "/admin/viewAllWithRoles";
	}

}
