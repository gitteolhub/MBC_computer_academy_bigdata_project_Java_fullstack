package com.javateam.healthyFoodProject.repository;

import java.util.List;
import java.util.Map;

import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;
import com.javateam.healthyFoodProject.domain.MemberVO;

public interface MemberDAO {

	// 주어진 ID로 회원 정보를 조회
	MemberVO selectMemberById(String strId);

	// 새로운 회원을 데이터베이스에 추가
	boolean insertMember(MemberVO objMemberVO);

	// 회원정보 중복 점검(회원 가입)
	boolean hasMemberByFld (String strField, String strValue);

	// 회원정보 수정
	boolean updateMember (MemberVO objMemberVO);

	// 회원정보 중복 점검(수정)
	boolean hasMemberForUpdate(String strId, String strField, String strValue);

	// 회원 Role 생성
	boolean insertRole(String strNum, String strId, String strRole);

	// 회원 Role 삭제
	boolean deleteRoles(String strId);

	// 회원 정보 삭제
	boolean deleteMemberById(String strId);

	// 회원 role 정보 조회
	List<String> selectRolesById(String strId);

	// 회원 role 정보 삭제
	void deleteRoleById(String strId, String strRole);

	// 회원 enabled 상태 변경
	void changeEnabled(String strId, int intEnabled);

	// 회원정보 조회(role 포함)
	Map<String, Object> selectMemberByFld(String strField, Object strValue);

	// 페이징에 의해(페이지 별) 회원정보 조회(검색)
	List<MemberVO> selectMembersByPaging(int intPage, int intLimit);

	// 전체 회원정보 조회
	List<MemberVO> selectAllMembers();

	// 회원정보 검색(페어링)
	List<Map<String, Object>> selectMembersBySearchingAndPaging(String strSearchKey,String strSearchWord, int intPage, int intLimit, String strIsLikeOrEquals,
																String strOrdering );

	// 전체 회원수 조회
	int selectCountAll();

	// 검색된 총 회원정보 수 조회
	int selectCountBySearching(String strSearchKey, String strSearchWord);

	// 회원가입시 초기foodMenu 정보 저장
	void updateInitializingFoodMenu(MemberVO objMemberVO);

}
