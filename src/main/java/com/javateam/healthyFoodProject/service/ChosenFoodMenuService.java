package com.javateam.healthyFoodProject.service;

public interface ChosenFoodMenuService {

	// 선택된 식단을 데이터베이스에 추가
	boolean insertChosenFoodMenu(String strId, String strFoodMenu, String strFoodMenuResult);

}
