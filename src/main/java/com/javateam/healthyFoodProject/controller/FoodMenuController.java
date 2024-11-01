package com.javateam.healthyFoodProject.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javateam.healthyFoodProject.service.ChosenFoodMenuService;
import com.javateam.healthyFoodProject.service.CustomOAuth2UserService;
import com.javateam.healthyFoodProject.service.JsonService;
import com.javateam.healthyFoodProject.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class FoodMenuController {

	@Autowired
	private ChosenFoodMenuService chosenFoodMenuService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;

	@Autowired
	JsonService jsonService;	// @@Autowired 로 변경

	private String foodMenu;

	// 선택할 foodMenu 조회
	@GetMapping("/foodMenu/viewJson")
	@ResponseBody
	public ResponseEntity<String> showFoodMenu(@RequestParam String strId) {
		log.info("[showFoodMenu]");

		String result = memberService.selectFoodMenuById(strId);

		if(result == null) {
			result = "당뇨 식단 메뉴를 찾을 수 없습니다";
		} else {
			result = foodMenu;
		}

		return new ResponseEntity<>(result,HttpStatus.OK);
	}

	public String showFoodData(String strId) {
		log.info("[showFoodMenuResult]");

		String result = chosenFoodMenuService.selectChosenFoodMenuById(strId).getFoodmenu();

		return result;
	}

	public String showFoodMenuResult(String strId) {
		log.info("[showFoodMenuResult]");

		String result = chosenFoodMenuService.selectChosenFoodMenuById(strId).getFoodmenuResult();

		return result;
	}

	// chosenFoodMenu 데이터를 map 형식으로
		public String mergeFoodData (String strId, String newChosenFoodData){
			String data = showFoodData(strId);
			String foodData;

			if(data == null) {
				foodData = "[" + newChosenFoodData + "]";
			} else {
				foodData = data;
				log.info("[FoodMenuController][dataToMap]foodData: ", foodData);
				foodData = foodData + ",";
				foodData = foodData + "[" + newChosenFoodData + "]";

			}

			return foodData;
		}

	//
	public String mergeFoodResultData (String strId, String newChosenFoodMenuResult) {
		String data = showFoodMenuResult(strId);
		log.info("[data null 여부]: {}", data == null);

		String foodDataResult;

		if(data == null) {
			foodDataResult = "[" + newChosenFoodMenuResult + "]";
		} else {
			foodDataResult = data;
			log.info("[FoodMenuController][mergeFoodResultData]foodDataResult: {}", foodDataResult);
			foodDataResult = foodDataResult.replace("[", "").replace("]", "") + "," + newChosenFoodMenuResult;
			foodDataResult = "[" + foodDataResult + "]";
			log.info("[FoodMenuController][foodDataResult]: {}", foodDataResult);

		}

		return foodDataResult;
	}

	// 선택할 foodMenu 조회
	@GetMapping("/foodMenu/view")
	public String showFoodMenu(@RequestParam String strId, Model model) {
		log.info("[showFoodMenu]");

		// 자체 회원의 foodMenu 조회
		foodMenu = memberService.selectFoodMenuById(strId);

		if(foodMenu != null) {

			// 자체 회원일 경우
			String[] menuItems = processFoodMenu(foodMenu);
			model.addAttribute("menuItems", menuItems);
			model.addAttribute("foodMenu", menuItems[0]); // 첫번재 음식

		} else {
			// 소셜 회원일 경우
			foodMenu = customOAuth2UserService.selectFoodMenuBySocialId(Integer.parseInt(strId));
			if(foodMenu == null) {
				model.addAttribute("msg", "당뇨 식단 메뉴를 찾을 수 없습니다");
			} else {
//				model.addAttribute("foodMenu", processFoodMenu(foodMenu));
				String[] menuItems = processFoodMenu(foodMenu);
				model.addAttribute("menuItems", menuItems);
				model.addAttribute("foodMenu", menuItems[0]); // 첫번재 음식
			}
		}
		return "foodMenu";
	}

	private String[] processFoodMenu(String strFoodMenu) {
		String cleanMenu = strFoodMenu.replace("[[", "").replace("]]", "");
		String[] menuItems = cleanMenu.split("\\],\\[");
		log.info("[FoodMenuController][processFoodMenu]");

		for (int i = 0; i < menuItems.length; i++) {
			menuItems[i] = menuItems[i].replace("[", "").replace("]", "").trim();
		}

		return menuItems;
	}

	// 식단을 좋아할 경우
	@PostMapping("/foodMenu/like") // TODO 임의로 정함(나중에 수정)
	@ResponseBody
	public ResponseEntity<String> likeFoodMenu(@RequestParam("strId") String strId, @RequestParam("foodMenu") String foodMenu, @RequestParam("foodMenuIndex") String foodMenuIndex) {

		log.info("[FoodMenuController][likeFoodMenu]");
		String msg = "";
		String updatingFoodData = mergeFoodData(strId, foodMenu);
		String updatingFoodDataResult = mergeFoodResultData(strId, "1");

		boolean success = chosenFoodMenuService.insertChosenFoodMenu(strId, updatingFoodData, updatingFoodDataResult);
		msg = success ? "회원이 좋아하는 식단입니다." : "에러(좋아하는 식단)";

		int foodMenuIndexInt = Integer.valueOf(foodMenuIndex);
		log.info("[foodMenuIndexInt]: {}", foodMenuIndexInt);

		if(foodMenuIndexInt == 0) {
			jsonService.saveChosenFoodMenuJson(strId);
			log.info("[saveChosenFoodMenuJson 실행 끝]");

		} else if (foodMenuIndexInt == 1) {

			// social Id일 경우
			// 아이디 첫 글지가 (0~9)
			if(strId.charAt(0) >= 48 && strId.charAt(0) <= 57) {
				int idInteger = Integer.valueOf(strId);
				customOAuth2UserService.updateFoodMenuBySocialUser(idInteger);
			// 자제 로그인 Id일 경우
			} else {
				memberService.updateFoodMenuByUser(strId);
			}
		}

		log.info("[FoodMenuController][msg]: {}", msg);

		// 결과를 보여줄 뷰 이름
		//		return "/foodMenu/result"; // TODO 임의로 정함(나중에 수정)
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	// 식단을 싫어할 경우
	@PostMapping("/foodMenu/dislike") // TODO 임의로 정함(나중에 수정)
	public ResponseEntity<String> dislikeFoodMenu(@RequestParam String strId, @RequestParam String foodMenu, @RequestParam String foodMenuIndex) {

		log.info("[FoodMenuController][dislikeFoodMenu]");
		String msg = "";
		String updatingFoodData = mergeFoodData(strId, foodMenu);
		String updatingFoodDataResult = mergeFoodResultData(strId, "0");

		boolean success = chosenFoodMenuService.insertChosenFoodMenu(strId, updatingFoodData, updatingFoodDataResult);
		msg = success ? "회원이 안 좋아하는 식단입니다." : "에러(안 좋아하는 식단)";

		int foodMenuIndexInt = Integer.valueOf(foodMenuIndex);
		log.info("[foodMenuIndexInt]: {}", foodMenuIndexInt);

		if(foodMenuIndexInt == 0) {
			jsonService.saveChosenFoodMenuJson(strId);
			log.info("[saveChosenFoodMenuJson 실행 끝]");

		} else if (foodMenuIndexInt == 1) {

			// social Id일 경우
			// 아이디 첫 글지가 (0~9)
			if(strId.charAt(0) >= 48 && strId.charAt(0) <= 57) {
				int idInteger = Integer.valueOf(strId);
				customOAuth2UserService.updateFoodMenuBySocialUser(idInteger);
			// 자제 로그인 Id일 경우
			} else {
				memberService.updateFoodMenuByUser(strId);
			}
		}

		log.info("[FoodMenuController][msg]: {}", msg);

		// 결과를 보여줄 뷰 이름
		//		return "/foodMenu/result"; // TODO 임의로 정함(나중에 수정)
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	// 당뇨식단이 아닌 경우
	@PostMapping("/foodMenu/refresh") // TODO 임의로 정함(나중에 수정)
	public ResponseEntity<String> refreshFoodMenu(@RequestParam String strId, @RequestParam String foodMenu, @RequestParam String foodMenuIndex) {

		log.info("[FoodMenuController][refreshFoodMenu]");
		String msg = "";
		String updatingFoodData = mergeFoodData(strId, foodMenu);
		String updatingFoodDataResult = mergeFoodResultData(strId, "-1");

		boolean success = chosenFoodMenuService.insertChosenFoodMenu(strId, updatingFoodData, updatingFoodDataResult);
		msg = success ? "당뇨식단이 아닙니다." : "에러(당뇨식단이 아닙니다)";

		int foodMenuIndexInt = Integer.valueOf(foodMenuIndex);
		log.info("[foodMenuIndexInt]: {}", foodMenuIndexInt);

		if(foodMenuIndexInt == 0) {
			jsonService.saveChosenFoodMenuJson(strId);
			log.info("[saveChosenFoodMenuJson 실행 끝]");

		} else if (foodMenuIndexInt == 1) {

			// social Id일 경우
			// 아이디 첫 글지가 (0~9)
			if(strId.charAt(0) >= 48 && strId.charAt(0) <= 57) {
				int idInteger = Integer.valueOf(strId);
				customOAuth2UserService.updateFoodMenuBySocialUser(idInteger);
			// 자제 로그인 Id일 경우
			} else {
				memberService.updateFoodMenuByUser(strId);
			}
		}

		log.info("[FoodMenuController][msg]: {}", msg);

		// 결과를 보여줄 뷰 이름
		//		return "/foodMenu/result"; // TODO 임의로 정함(나중에 수정)
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}
}
