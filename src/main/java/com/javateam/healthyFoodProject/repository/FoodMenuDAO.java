package com.javateam.healthyFoodProject.repository;

import com.javateam.healthyFoodProject.domain.FoodMenuVO;

public interface FoodMenuDAO {

	// 선택된 식단을 데이터베이스에 추가
	void insertChosenFoodMenu(FoodMenuVO objFoodMenuVO);

	// 선택된 식단을 수정
	void updateChosenFoodMenu(FoodMenuVO objFoodMenuVO);



}
