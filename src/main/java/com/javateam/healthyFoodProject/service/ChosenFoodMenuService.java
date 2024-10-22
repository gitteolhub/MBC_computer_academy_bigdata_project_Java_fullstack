package com.javateam.healthyFoodProject.service;

public interface ChosenFoodMenuService {

	// 회원 아이디를 선택된 식단 데이터베이스에 추가
	boolean insertIdChosenFoodMenu(String strId);

	// 선택된 식단을 데이터베이스에 추가
	boolean insertChosenFoodMenu(String strId, String strFoodMenu, String strFoodMenuResult);

}
