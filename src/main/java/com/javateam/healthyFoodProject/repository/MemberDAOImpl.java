package com.javateam.healthyFoodProject.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.healthyFoodProject.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MemberDAOImpl implements MemberDAO {

	@Autowired
	public SqlSession sqlSession;

	private static final String MAPPER_PATH = "mapper.healthyFoodMapper.";

	// 주어진 ID로 회원 정보를 조회
	@Override
	public MemberVO selectMemberById(String strId) {

		return sqlSession.selectOne(MAPPER_PATH + "selectMemberById",strId);
	}

	// 새로운 회원을 데이터베이스에 추가
	@Override
	public boolean insertMember(MemberVO objMemberVO) {

		boolean blRetVal = false;

		try {
			int intResult = sqlSession.insert(MAPPER_PATH + "insertMember", objMemberVO);
			blRetVal = intResult == 1 ? true : false;

		} catch (Exception ex) {
			log.error("[MemberDAOImpl][insertMember] Exception : " + ex);
		}
		return blRetVal;
	}

	// 회원정보 중복 점검(회원 가입)
	@Override
	public boolean hasMemberByFld(String strField, String strValue) {

		// 중독 체크하기 위해 맵 생성
		Map<String, String> memberDatabase = new HashMap<>();

		memberDatabase.put("fld", strField);	// 필드 이름 추가
		memberDatabase.put("val", strValue); 	// 필드 값 추가

		// name, gender 중복 허용
		if (strField.equals("NAME") || strField.equals("GENDER")) {

			return false;

		} else {

			return(int) sqlSession.selectOne(MAPPER_PATH + "hasMemberByFld", memberDatabase) == 1 ? true: false;
		}

	}

	// 회원정보 수정
	@Override
	public boolean updateMember (MemberVO objMemberVO) {

		boolean blRetVal = false;

		try {
			//기존 회원정보 존재 여부 점검
			int intResult = this.selectMemberById(objMemberVO.getId()) != null ? 1 : 0;

		if(intResult == 0) {
			throw new Exception("회원정보가 존재하지 않습니다.");
		}
		sqlSession.update(MAPPER_PATH + "updateMember",objMemberVO);

		blRetVal = true;
		} catch (Exception ex) {
			log.error("[MemberDao][updateMember] Exception : {}", ex);
			ex.printStackTrace();
		}
		return blRetVal;
	}

	// 회원정보 중복 점검(수정)
	@Override
	public boolean hasMemberForUpdate(String strId, String strField, String strValue) {

		Map<String, String> map = new HashMap<> ();

		map.put("id",  strId);
		map.put("fld", strField);
		map.put("val", strValue);

		return (int)sqlSession.selectOne(MAPPER_PATH + "hasMemberForUpdate", map) == 1 ? true : false;
	}

	// 회원 Role 생성
	@Override
	public boolean insertRole(String strNum, String strId, String strRole) {

		boolean blRetVal = false;

		try {
			Map<String, String> map = new HashMap<>();

			map.put("num",  strNum);
			map.put("id",   strId);
			map.put("role", strRole);

			sqlSession.selectList(MAPPER_PATH + "insertRole", map);
			blRetVal = true;

		} catch (Exception ex) {
			log.error("[MemberDao][insertRole] Exception = " + ex.getMessage(), ex);
			ex.printStackTrace();

		}
		return blRetVal;
	}

	// 회원 Role 삭제
	@Override
	public boolean deleteRoles(String strId) {

		boolean blRetVal = false;

		try {

			sqlSession.delete(MAPPER_PATH + "deleteRoles", strId);
			blRetVal = true;

		} catch(Exception ex) {
			log.error("[MemberDAOImpl][deleteRoles] Exception: {}", ex);
			ex.printStackTrace();
		}

		return blRetVal;
	}

	// 회원 정보 삭제
	@Override
	public boolean deleteMemberById(String strId) {

		boolean blRetVal = false;

		try {

			sqlSession.delete(MAPPER_PATH + "deleteMemberById", strId);
			blRetVal = true;

		} catch(Exception ex) {
			log.error("[deleteMemberById] Exception = " + ex);
			ex.printStackTrace();
		}

		return blRetVal;
	}

	// 회원 role 정보 조회
	@Override
	public List<String> selectRolesById(String strId) {

		return sqlSession.selectList(MAPPER_PATH  + "selectRolesById", strId);
	}

	// 회원 role 정보 삭제
	@Override
	public void deleteRoleById(String strId, String strRole) {

		Map<String, String> map = new HashMap<>();
		map.put("id",  strId);
		map.put("role", strRole);

		sqlSession.delete(MAPPER_PATH + "deleteRoleById", map);
	}

	// 회원 enabled 상태 변경
	@Override
	public void changeEnabled(String strId, int intEnabled) {

		Map<String, Object> map = new HashMap<>();
		map.put("id", strId);
		map.put("enabled", intEnabled);

		log.info("상태 정보 : {}", intEnabled);

		sqlSession.update(MAPPER_PATH  + "changeEnabled", map);
	}

	// 회원정보 조회(role 포함)
	@Override
	public Map<String,Object> selectMemberByFld(String strField, Object objValue) {

		Map<String,Object> map = new HashMap<>();

		map.put("fld", strField);
		map.put("val", objValue);

		return sqlSession.selectOne(MAPPER_PATH + "selectMemberByFld", map);
	}

	// 페이징에 의해(페이지 별) 회원정보 조회(검색)
	@Override
	public List<MemberVO> selectMembersByPaging(int intPage, int intLimit){

		HashMap<String, Object> hashMap = new HashMap<>();

		try {
			hashMap.put("page",  intPage);
			hashMap.put("limit", intLimit);

			return sqlSession.selectList(MAPPER_PATH + "selectMembersByPaging", hashMap);

		} catch (Exception ex) {

			log.error("[MemberDao][selectMembersByPaging] Exception: {}", ex);
			ex.printStackTrace();

			return null;
		}
	}

	// 전체 회원정보 조회
	@Override
	public List<MemberVO> selectAllMembers(){

		return sqlSession.selectList(MAPPER_PATH + "selectAllMembers");
	}

	// 회원정보 검색(페어링)
	@Override
	public List<Map<String, Object>> selectMembersBySearchingAndPaging(String strSearchKey,String strSearchWord, int intPage, int intLimit, String strIsLikeOrEquals,
																	   String strOrdering ){

		Map<String, Object> map = new HashMap<>();

		map.put("searchKey",      strSearchKey);
		map.put("searchWord",     strSearchWord);
		map.put("page",           intPage);
		map.put("limit",          intLimit);
		map.put("isLikeOrEquals", strIsLikeOrEquals);

		map.put("ordering", strOrdering);

		log.info("인자 출력: ");
		map.entrySet().forEach(x -> {log.info(x + "");});

		return sqlSession.selectList(MAPPER_PATH + "selectMembersBySearchingAndPaging" , map);
	}

	// 전체 회원수 조회
	@Override
	public int selectCountAll() {

		return sqlSession.selectOne(MAPPER_PATH + "selectCountAll");
	}

	// 검색된 총 회원정보 수 조회
	@Override
	public int selectCountBySearching(String strSearchKey, String strSearchWord) {

		Map<String, Object> map = new HashMap<>();
		map.put("SearchKey", strSearchKey);
		map.put("searchWord", strSearchWord);

		return (int)sqlSession.selectOne(MAPPER_PATH + "selectCountBySearching", map);
	}

	// 회원가입시 초기foodMenu 정보 저장
	@Override
	public void updateInitializingFoodMenu(MemberVO objMemberVO) {
		sqlSession.update(MAPPER_PATH + "updateInitializingFoodMenu", objMemberVO);
	}

	// 선택할 foodMenu 조회
	@Override
	public String selectFoodMenuById(String strId) {

		log.info("[MemberDAO][selectFoodMenuById]");
		return sqlSession.selectOne(MAPPER_PATH + "selectFoodMenuById", strId);
	}

	// 사용자별로 바뀔 식단 업데이트
	@Override
	public boolean updateFoodMenuByUser(MemberVO objMemberVO) {
		boolean blRetVal = false;

		try{
			sqlSession.update(MAPPER_PATH + "updateFoodMenuByUser", objMemberVO);
			blRetVal = true;
		} catch(Exception ex) {
			log.error("[updateFoodMenuByUser] Exception: {}", ex);
			ex.printStackTrace();
		}
		return blRetVal;
	}

}