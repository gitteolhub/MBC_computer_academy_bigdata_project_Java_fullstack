package com.example.teamproject.repository;

import java.util.List;

import com.example.teamproject.domain.MemberVO;
/**
 * 
 * @author USER
 *
 */
public interface MemberDAO {

	/**
	 * 개별 회원 정보 조회(검색)
	 * @param id 회원 아이디
	 * @return 회원 정보
	 */

	MemberVO selectMemberById(String id);

	/**
	 * 페이징에 의한(페이지 별로) 회원정보 조회(검색)
	 *
	 * @param page 페이지
	 * @param limit 페이지당 회원정보 갯수
	 * @return 회원정보(들)
	 */
	
	List<MemberVO> selectMembersByPaging(int page, int limit);

	/**
	 * 전체 회원정보 조회
	 * @return
	 */
	List<MemberVO> selectAllMembers();

	/**
	 * 회원정보 삽입(생성)
	 *
	 * @param memberVO 회원정보
	 * @return 삽입된(생성된) 레코드 수(int) / 삽입(생성) 여부(boolean)
	 * ex) 1/true(레코드 생성), 0/false(레코드 미생성)
	 */
}