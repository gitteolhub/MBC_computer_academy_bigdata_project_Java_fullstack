package com.javateam.healthyFoodProject.service;

import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;

public interface ChosenFoodMenuService {

	// 선택된 식단을 데이터베이스에 추가
	boolean insertChosenFoodMenu(String strId, String strFoodMenu, String strFoodMenuResult);

	// ID로 선택된 식단 조회
	ChosenFoodMenuVO selectChosenFoodMenuById(String strId);
}
