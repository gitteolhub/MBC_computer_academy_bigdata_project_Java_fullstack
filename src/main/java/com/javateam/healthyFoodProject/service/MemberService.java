 package com.javateam.healthyFoodProject.service;

import java.util.Map;

import com.javateam.healthyFoodProject.domain.MemberVO;
import com.javateam.healthyFoodProject.domain.Role;

public interface MemberService {
	
	// 아이디로 회원 정보를 조회
	MemberVO selectMemberById(String strId);
	
	// 중복 아이디 확인후 새로운 회원 추가
	boolean insertMember(MemberVO objMemberVO);
	
	// 회원정보 수정
	boolean updateMember (MemberVO objMemberVO);
	
	// 회원정보 삭제
	boolean deleteMember(String strId);
	
	// 회원정보 중복 점검(회원 가입)
	boolean hasMemberByFld (String strField, String strValue);
	
	// 회원정보 중복 점검(수정)
	boolean hasMemberForUpdate(String strId, String strField, String strValue);
	
	// 회원 role 생성
	boolean insertRole(Role role);
	
	// 회원 role 수정 (관리자 권한)
	boolean updateRoles(String strId, boolean blRoleUserYn, boolean blRoleAdminYn);
	
	// 회원 role 삭제
	boolean deleteRoleById(String strId, String strRole);
	
	// 회원 enabled 상태 변경
	boolean changeEnabled(String strId, int intEnabled);
	
}
