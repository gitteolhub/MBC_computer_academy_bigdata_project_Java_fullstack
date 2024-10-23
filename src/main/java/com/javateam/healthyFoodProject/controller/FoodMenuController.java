package com.javateam.healthyFoodProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.healthyFoodProject.service.ChosenFoodMenuService;
import com.javateam.healthyFoodProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class FoodMenuController {

	@Autowired
	private ChosenFoodMenuService chosenFoodMenuService;

	@Autowired
	private MemberService memberService;

	// 선택할 foodMenu 조회
	@GetMapping("/foodmenu/view")
	public String showFoodMenu(@RequestParam String strud, Model model) {

		String foodMenu = memberService.selectFoodMenuById(strud);

		if(foodMenu == null) {
			model.addAttribute("msg", "당뇨 식단 메뉴를 찾을 수 없습니다");
		} else {
			model.addAttribute("foodMenu", foodMenu);
		}

		return "foodMenu";
	}


	// 식단을 좋아할 경우
	@PostMapping("/foodMenu/like") // TODO 임의로 정함(나중에 수정)
	public String likeFoodMenu(@RequestParam String strId, @RequestParam String foodMenu, Model model) {

		boolean success = chosenFoodMenuService.insertChosenFoodMenu(strId, foodMenu, "1");
		log.info("[FoodMenuController][likeFoodMenu]");

		model.addAttribute("msg", success ? "회원이 좋아하는 식단입니다." : "에러(좋아하는 식단)");

		// 결과를 보여줄 뷰 이름
		return "/foodMenu/result"; // TODO 임의로 정함(나중에 수정)
	}

	// 식단을 싫어할 경우
	@PostMapping("/foodMenu/dislike") // TODO 임의로 정함(나중에 수정)
	public String dislikeFoodMenu(@RequestParam String strId, @RequestParam String foodMenu, Model model) {

		boolean success = chosenFoodMenuService.insertChosenFoodMenu(strId, foodMenu, "0");
		log.info("[FoodMenuController][dislikeFoodMenu]");

		model.addAttribute("msg", success ? "회원이 안 좋아하는 식단입니다." : "에러(안 좋아하는 식단)");

		// 결과를 보여줄 뷰 이름
		return "/foodMenu/result"; // TODO 임의로 정함(나중에 수정)
	}

	// 당뇨식단이 아닌 경우
	@PostMapping("/foodMenu/refresh") // TODO 임의로 정함(나중에 수정)
	public String refreshFoodMenu(@RequestParam String strId, @RequestParam String foodMenu, Model model) {

		boolean success = chosenFoodMenuService.insertChosenFoodMenu(strId, foodMenu, "-1");
		log.info("[FoodMenuController][refreshFoodMenu]");

		model.addAttribute("msg", success ? "당뇨식단이 아닙니다." : "에러(당뇨식단이 아닙니다.)");

		// 결과를 보여줄 뷰 이름
		return "/foodMenu/result"; // TODO 임의로 정함(나중에 수정)
	}

}
