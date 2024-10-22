package com.javateam.healthyFoodProject.service;

import java.util.List;

import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;

public interface ChosenFoodMenuService {

	// 회원 아이디를 선택된 식단 데이터베이스에 추가
	boolean insertIdChosenFoodMenu(String strId);

	// 선택된 식단을 데이터베이스에 추가
	boolean insertChosenFoodMenu(String strId, String strFoodMenu, String strFoodMenuResult);

//	// 선택된 식단을 수정(업데이트)
//	boolean updateChosenFoodMenu(ChosenFoodMenuVO objChosenFoodMenuVO);

//	// 선택된 식단 삭제
//	boolean deleteChosenFoodMenu(ChosenFoodMenuVO objChosenFoodMenuVO);


}
