package com.javateam.healthyFoodProject.repository;

import java.util.List;

import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;

public interface ChosenFoodMenuDAO {

	// ID로 선택된 식단 조회
	ChosenFoodMenuVO selectChosenFoodMenuById(String strId);

	// 회원 아이디를 선택된 식단 데이터베이스에 추가
	void insertIdChosenFoodMenu(String strId);

	// 선택된 식단을 데이터베이스에 추가
	boolean insertChosenFoodMenu(ChosenFoodMenuVO objChosenFoodMenuVO);

	// 선택된 식단 수정
	boolean updateChosenFoodMenu(ChosenFoodMenuVO objChosenFoodMenuVO);

	// 선택된 식단 삭제
	boolean deleteChosenFoodMenuById(ChosenFoodMenuVO objChosenFoodMenuVO);

	// 선택된 식단 전체 조회
	List<ChosenFoodMenuVO> selectAllFoodMenu();



}
