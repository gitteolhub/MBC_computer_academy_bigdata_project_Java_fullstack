package com.javateam.healthyFoodProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javateam.healthyFoodProject.service.ChosenFoodMenuService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChosenFoodMenuController {

	@Autowired
	private ChosenFoodMenuService chosenFoodMenuService;

	// 식단을 좋아할 경우
	@PostMapping("/foodMenu") // TODO 임의로 정함(나중에 수정)
	public String likeFoodMenu(@RequestParam String strId, @RequestParam String foodMenu, Model model) {

		boolean success = chosenFoodMenuService.insertChosenFoodMenu(strId, foodMenu, "1");
		log.info("[ChosenFoodMenuController][likeFoodMenu]");

		model.addAttribute("msg", success ? "회원이 좋아하는 식단입니다." : "에러(좋아하는 식단)");

		// 결과를 보여줄 뷰 이름
		return "/foodMenu/result"; // TODO 임의로 정함(나중에 수정)
	}

	// 식단을 싫어할 경우
	@PostMapping("/foodMenu") // TODO 임의로 정함(나중에 수정)
	public String dislikeFoodMenu(@RequestParam String strId, @RequestParam String foodMenu, Model model) {

		boolean success = chosenFoodMenuService.insertChosenFoodMenu(strId, foodMenu, "0");
		log.info("[ChosenFoodMenuController][dislikeFoodMenu]");

		model.addAttribute("msg", success ? "회원이 안 좋아하는 식단입니다." : "에러(안 좋아하는 식단)");

		// 결과를 보여줄 뷰 이름
		return "/foodMenu/result"; // TODO 임의로 정함(나중에 수정)
	}

	// 당뇨식단이 아닌 경우
	@PostMapping("/foodMenu") // TODO 임의로 정함(나중에 수정)
	public String refreshFoodMenu(@RequestParam String strId, @RequestParam String foodMenu, Model model) {

		boolean success = chosenFoodMenuService.insertChosenFoodMenu(strId, foodMenu, "-1");
		log.info("[ChosenFoodMenuController][refreshFoodMenu]");

		model.addAttribute("msg", success ? "당뇨식단이 아닙니다." : "에러(당뇨식단이 아닙니다.)");

		// 결과를 보여줄 뷰 이름
		return "/foodMenu/result"; // TODO 임의로 정함(나중에 수정)
	}

}
