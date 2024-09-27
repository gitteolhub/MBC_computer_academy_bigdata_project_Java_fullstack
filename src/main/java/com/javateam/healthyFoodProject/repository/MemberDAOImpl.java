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
	
	private static final String MAPPER_PATH="mapper.healthyFoodMapper.";
	
	@Override
	public MemberVO selectMemberById(String strId) {
		
		return sqlSession.selectOne(MAPPER_PATH + "selectMemberById",strId);
	}
	
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
	
	@Override
	public boolean hasMemberForUpdate(String strId, String strField, String strValue) {
		
		Map<String, String> map = new HashMap<> ();
		
		map.put("id",  strId);
		map.put("fld", strField);
		map.put("val", strValue);
		
		return (int)sqlSession.selectOne(MAPPER_PATH + "hasMemberForUpdate", map) == 1 ? true : false;
	}
	
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
	
	@Override
	public boolean deleteRoles(String strId) {
	
		boolean blRetVal = false;
		
		try {
			
			sqlSession.delete(MAPPER_PATH + "deleteRoles", strId);
			blRetVal = true;
			
		} catch(Exception ex) {
			log.error("[MemberDAOImpl][deleteRoles] Exception = " + ex);
			ex.printStackTrace();
		}
		
		return blRetVal;
	}
	
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
	
	@Override
	public List<String> selectRolesById(String strId) {
		
		return sqlSession.selectList(MAPPER_PATH  + "selectRolesById", strId);
	}
	
	@Override
	public void deleteRoleById(String strId, String strRole) {

		Map<String, String> map = new HashMap<>();
		map.put("id",  strId);
		map.put("role", strRole);

		sqlSession.delete(MAPPER_PATH + "deleteRoleById", map);
	}
	
	@Override
	public void changeEnabled(String strId, int intEnabled) {

		Map<String, Object> map = new HashMap<>();
		map.put("id", strId);
		map.put("enabled", intEnabled);

		log.info("상태 정보 : {}", intEnabled);

		sqlSession.update(MAPPER_PATH  + "changeEnabled", map);
	}
}