package com.javateam.healthyFoodProject.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ChosenFoodMenuDAOImpl implements ChosenFoodMenuDAO{

	@Autowired
	public SqlSession sqlSession;

	private static final String MAPPER_PATH = "mapper.healthyFoodMapper.";

	// ID로 선택된 식단 조회
	@Override
	public ChosenFoodMenuVO selectChosenFoodMenuById(String strId) {
		return sqlSession.selectOne(MAPPER_PATH + "selectChosenFoodMenuById", strId);
	}

	// 회원 아이디를 선택된 식단 데이터베이스에 추가
	@Override
	public boolean insertIdChosenFoodMenu(String strId) {

		boolean blRetVal = false;

		try {
			log.info("[ChosenFoodMenuDAOImpl][insertIdChosenFoodMenu]: {}", strId);
			sqlSession.insert(MAPPER_PATH + "insertIdChosenFoodMenu", strId);

			blRetVal = true;
		} catch(Exception ex) {
			log.error("[ChosenFoodMenuDAOImpl][insertIdChosenFoodMenu] Exception: {}", ex);
		}
		return blRetVal;
	}

	// 선택된 식단을 데이터베이스에 추가
	@Override
	public void insertChosenFoodMenu(String strId, String strFoodMenu, String strFoodMenuResult) {
		Map<String, Object> params = new HashMap<>();
		params.put("strId", strId);
		params.put("strFoodMenu", strFoodMenu);
		params.put("strFoodMenuResult", strFoodMenuResult);

		sqlSession.insert(MAPPER_PATH + "insertChosenFoodMenu", params);

	}

	// 선택된 식단 삭제
	@Override
	public boolean deleteChosenFoodMenuById(String strId) {

		boolean blRetVal = false;

		try {

			log.info("[ChosenFoodMenuDAOImpl][deleteChosenFoodMenuById]: {}", strId);
			sqlSession.delete(MAPPER_PATH + "deleteChosenFoodMenuById", strId);

			blRetVal = true;

		} catch(Exception ex) {
			log.error("[ChosenFoodMenuDAOImpl][deleteChosenFoodMenuById] Exception: {}", ex);
		}
		return blRetVal;
	}

	// 선택된 식단 전체 조회
	@Override
	public List<ChosenFoodMenuVO> selectAllFoodMenu() {

		return sqlSession.selectList(MAPPER_PATH + "selectAllFoodMenu");
	}

}
