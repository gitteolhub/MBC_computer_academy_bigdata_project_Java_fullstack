package com.javateam.healthyFoodProject.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;
import com.javateam.healthyFoodProject.domain.SocialUser;

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
	public void insertIdChosenFoodMenu(String strId) {
		sqlSession.insert(MAPPER_PATH + "insertIdChosenFoodMenu", strId);
	}

	// 선택된 식단을 데이터베이스에 추가
	@Override
	public boolean insertChosenFoodMenu(ChosenFoodMenuVO objChosenFoodMenuVO) {

		boolean blRetVal = false;

		try {

			int intResult = sqlSession.insert(MAPPER_PATH + "insertChosenFoodMenu", objChosenFoodMenuVO);
			blRetVal = intResult == 1 ? true : false;

		} catch (Exception ex) {
			log.error("[ChosenFoodMenuDAOImpl][insertChosenFoodMenu] Exception: " + ex);
		}
		return blRetVal;
	}

	// 선택된 식단 수정
	@Override
	public boolean updateChosenFoodMenu(ChosenFoodMenuVO objChosenFoodMenuVO) {

		boolean blRetVal = false;

		try {
			int intResult = this.selectChosenFoodMenuById(objChosenFoodMenuVO.getId()) != null ? 1 : 0;

		if(intResult == 0) {
			throw new Exception("회원 Id가 없습니다.");
		}
		sqlSession.update(MAPPER_PATH + "updateChosenFoodMenu", objChosenFoodMenuVO);
		log.info("[ChosenFoodMenuDAOImpl][updateChosenFoodMenu]: {}", objChosenFoodMenuVO);

		blRetVal = true;

		} catch (Exception ex) {
			log.error("[ChosenFoodMenuDAOImpl][updateChosenFoodMenu] Exception: {}", ex);
			ex.printStackTrace();
		}
		return blRetVal;
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
