package com.javateam.healthyFoodProject.mainPack;

import org.springframework.http.ResponseEntity;

import com.javateam.healthyFoodProject.controller.FoodMenuController;

public class MainFoodData {

	public static void main(String[] args) {
		FoodMenuController foodMenuController = new FoodMenuController();
		ResponseEntity<String> responseData = foodMenuController.showFoodMenu("abcd1234");
		foodMenuController.dataToMap(responseData);
	}

}
