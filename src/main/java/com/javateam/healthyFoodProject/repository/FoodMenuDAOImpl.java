package com.javateam.healthyFoodProject.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.healthyFoodProject.domain.FoodMenuVO;

@Repository
public class FoodMenuDAOImpl implements FoodMenuDAO{

	@Autowired
	public SqlSession sqlSession;

	private static final String MAPPER_PATH = "mapper.healthyFoodMapper.";

	// 선택된 식단을 데이터베이스에 추가
	@Override
	public void insertChosenFoodMenu(FoodMenuVO objFoodMenuVO) {
		sqlSession.insert(MAPPER_PATH + "insertChosenFoodMenu", objFoodMenuVO);
	}

	@Override
	public void updateChosenFoodMenu(FoodMenuVO objFoodMenuVO) {
		sqlSession.insert(MAPPER_PATH + "updateChosenFoodMenu", objFoodMenuVO);

	}
}
