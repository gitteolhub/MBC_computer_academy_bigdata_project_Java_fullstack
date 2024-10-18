package com.javateam.healthyFoodProject.service;

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


//	 //memberService에서 함
//	 //회원 아이디를 선택된 식단 데이터베이스에 추가
//	@Override
//	public boolean insertIdChosenFoodMenu(String strId) {
//		boolean blRetVal = false;
//
//		try {
//			chosenFoodMenuDAO.insertIdChosenFoodMenu(strId);
//
//			blRetVal = true;
//
//		} catch (Exception ex) {
//			log.error("[ChosenFoodMenuServiceImpl][insertIdChosenFoodMenu] Exception: {}", ex);
//		}
//
//		return blRetVal;
//	}

	// 선택된 식단을 데이터베이스에 추가
	@Override
	public boolean insertChosenFoodMenu(ChosenFoodMenuVO objChosenFoodMenuVO) {
		boolean blRetVal = false;

		try {
			chosenFoodMenuDAO.insertChosenFoodMenu(objChosenFoodMenuVO);
			blRetVal = true;
			log.info("[ChosenFoodMenuServiceImpl][insertChosenFoodMenu]: {}", objChosenFoodMenuVO);

		} catch (Exception ex) {
			log.error("[ChosenFoodMenuServiceImpl][insertChosenFoodMenu] Exception: {}", ex);
		}

		return blRetVal;
	}

	// 선택된 식단을 수정(업데이트)
	@Override
	public boolean updateChosenFoodMenu(ChosenFoodMenuVO objChosenFoodMenuVO) {
		boolean blRetVal = false;

		try {



		} catch (Exception ex) {

		}

		return blRetVal;
	}

//	@Override
//	public boolean deleteChosenFoodMenu(ChosenFoodMenuVO objChosenFoodMenuVO) {
//		boolean blRetVal = false;
//
//		try {
//			chosenFoodMenuDAO.deleteChosenFoodMenuById(objChosenFoodMenuVO);
//			blRetVal = true;
//
//		} catch(Exception ex){
//			log.error("[ChosenFoodMenuServiceImpl][deleteChosenFoodMenu] Exception: {}", ex);
//			ex.printStackTrace();
//
//		}
//		return blRetVal;
//	}

	// 선택된 식단 전체 조회
	@Override
	public List<ChosenFoodMenuVO> selectAllFoodMenu() {

		return chosenFoodMenuDAO.selectAllFoodMenu();
	}

}
