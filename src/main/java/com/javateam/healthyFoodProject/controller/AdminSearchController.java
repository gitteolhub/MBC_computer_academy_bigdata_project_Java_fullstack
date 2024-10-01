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
public class AdminSearchController {
	
	@Autowired
	public MemberService memberService;
	
	@GetMapping("/seacrhAllWithRoles")
	public String adminViewWithRoles (@RequestParam(value = "currentPage", defaultValue = "1",  required = true) int currentPage,
									  @RequestParam(value = "limit",       defaultValue = "20") int intLimit,
									  @RequestParam(value = "serchKey")    String strSearchKey,
									  @RequestParam(value = "serchWord")   String strSearchWord, Model model) {
		
		log.info("관리자 화면 검색: role 표시");
		List<Map<String, Object>> members = new ArrayList<>();
		
		String isLikeOrEquals = "";
		
		// 문자열 앞뒤 공백 제거
		strSearchWord = strSearchWord.trim();
		
		// 유사 검색(이름, 가입일)
		isLikeOrEquals = (strSearchKey.equals("name") || 
					 	  strSearchKey.equals("joindate")) ? "like" : "equals";
		
		// 성별 검색에 따른 검색값 변환
		if (strSearchKey.equals("gender")) {
			
			strSearchWord = strSearchWord.equals("남자") ? "남자" : "여자";
			
		} else if(strSearchKey.equals("role")) {
			
			strSearchWord = strSearchWord.equals("관리자") ? "ROLE_ADMIN" :
							strSearchWord.equals("회원")  ? "ROLE_USER"  : "없음"; 
		}
		
		log.info("[searchWord]: {}", strSearchWord);
		
		members = memberService.selectMembersBySearchingAndPaging(strSearchKey.toUpperCase(), strSearchWord, currentPage, intLimit, isLikeOrEquals, "DESC");
		
		// 총 "검색" 인원 수
		int listCount = memberService.selectCountBySearching(strSearchKey, strSearchWord);
		
		log.info("총 검색 인원수: {}", listCount);
		
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
		
		model.addAttribute("pageVO",  pageVO);
		model.addAttribute("members", members);
		model.addAttribute("searchKey",  strSearchKey);
		model.addAttribute("searchWord", strSearchWord);
		
		return "/admin/viewAllWithRoles";
		
	}
	

}
