package com.javateam.healthyFoodProject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;
import com.javateam.healthyFoodProject.domain.MemberVO;
import com.javateam.healthyFoodProject.repository.ChosenFoodMenuDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChosenFoodMenuServiceImpl implements ChosenFoodMenuService{

	@Autowired
	ChosenFoodMenuDAO chosenFoodMenuDAO;

	// 선택된 식단을 데이터베이스에 추가
	@Override
	public boolean insertChosenFoodMenu(String strId, String strFoodMenu, String  strFoodMenuResult) {
		boolean blRetVal = false;

		try {

			chosenFoodMenuDAO.insertChosenFoodMenu(strId, strFoodMenu, strFoodMenuResult);
			log.info("[ChosenFoodMenuServiceImpl][insertChosenFoodMenu]");
			blRetVal = true;

		} catch (Exception ex) {
			log.error("[ChosenFoodMenuServiceImpl][insertChosenFoodMenu] Exception: {}", ex);
		}

		return blRetVal;
	}

}
