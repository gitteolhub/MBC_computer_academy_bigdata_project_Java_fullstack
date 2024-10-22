package com.javateam.healthyFoodProject.service;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;
import com.javateam.healthyFoodProject.domain.MemberJsonVO;
import com.javateam.healthyFoodProject.domain.MemberVO;
import com.javateam.healthyFoodProject.domain.Role;
import com.javateam.healthyFoodProject.domain.SocialUser;
import com.javateam.healthyFoodProject.repository.ChosenFoodMenuDAO;
import com.javateam.healthyFoodProject.repository.MemberDAO;
import com.javateam.healthyFoodProject.repository.SocialUserMybatisDAO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

	@Autowired
	public PlatformTransactionManager dataSourceTransactionManager;
	public TransactionTemplate        transactionTemplate;

	@Autowired
	void setTransactionTemplate (PlatformTransactionManager transactionManager) {

		this.transactionTemplate = new TransactionTemplate(transactionManager);
	}

	@Autowired
	ChosenFoodMenuDAO chosenFoodMenuDAO;

	@Autowired
	MemberDAO memberDAO;

	@Autowired
	SocialUserMybatisDAO socialUserMybatisDAO;


	// 아이디로 회원 정보를 조회
	@Transactional (readOnly = true)
	@Override
	public MemberVO selectMemberById(String strId) {

		return memberDAO.selectMemberById(strId);
	}

	// 중복 아이디 확인후 새로운 회원 추가
	@Override
	public boolean insertMember(MemberVO objMemberVO) {

		return transactionTemplate.execute(new TransactionCallback<Boolean>(){

			@Override
			public Boolean doInTransaction(TransactionStatus objTransactionstatus) {

				boolean blRetVal = false;

				try {
					log.info("기존 회원 존재여부: {}", memberDAO.hasMemberByFld("ID", objMemberVO.getId()));

					// 중복 아이디가 존재하면 예외 발생
					if (memberDAO.hasMemberByFld("ID",objMemberVO.getId()) == true) {
						throw new Exception("중복되는 아이디가 존재합니다.");
					}
					log.info("[MemberService][insertMember]: {}", objMemberVO);

					// 비밀번호 암호화
					BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
					String encodePw = bCryptPasswordEncoder.encode(objMemberVO.getPw());
					objMemberVO.setPw(encodePw);
					objMemberVO.setEnabled(1);

					// 회원 정보 저장
					blRetVal = memberDAO.insertMember(objMemberVO);

					// foodmenu가 null인 경우 초기값 설정
					if(blRetVal && objMemberVO.getFoodmenu() == null) {

						//TODO FilePath 지정 필요
						// 회원가입시 초기 식단 추가
						JsonService jsonService = new JsonService();

						String InitializingFoodMenuFilePath="";
						String foodMenu = jsonService.readFoodMenuJson(InitializingFoodMenuFilePath);
						objMemberVO.setFoodmenu(foodMenu);
						memberDAO.updateInitializingFoodMenu(objMemberVO);
					}

				} catch (Exception ex) {
	 				log.error("[MemberService][insertMember] Exception: {}", ex);
					objTransactionstatus.setRollbackOnly();
				}

				if(blRetVal == true) {

				// 회원 Role 생성
				try {
					if (memberDAO.hasMemberByFld("ID", objMemberVO.getId()) == false) {
						throw new Exception ("회원정보가 존재하지 않습니다.");
					}
					log.info("[MemberService] Role 생성: {}", objMemberVO);

					// 가입된 회원 정보 조회
					MemberVO memberVO = memberDAO.selectMemberById(objMemberVO.getId());

					blRetVal = memberDAO.insertRole(memberVO.getNum(), memberVO.getId(), "ROLE_USER");

				} catch (Exception ex) {
					log.error("[MemberService][insertMember](Role) Exception: {}", ex);
					objTransactionstatus.setRollbackOnly();
				}

				}
				return blRetVal;
			}
		});
	}

	// 회원정보 수정
	@Override
	public boolean updateMember (MemberVO objmemberVO) {

		return transactionTemplate.execute(new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus transactionStatus)	{
				boolean blRetVal = false;

				try {
					//  기존 회원 존재 여부
					if(memberDAO.hasMemberByFld("ID", objmemberVO.getId()) == false) {
						throw new Exception("수정할 회원정보가 존재하지 않습니다.");
					}
					blRetVal = memberDAO.updateMember(objmemberVO);

				} catch (Exception ex) {
					log.error("[MemberService][updateMember] Exception : " + ex);
					transactionStatus.setRollbackOnly();
				}

				return blRetVal;
			}
		});
	}

	// 회원정보 삭제
	@Override
	public boolean deleteMember(String strId) {

		return transactionTemplate.execute(new TransactionCallback<Boolean>() {

			@Override
			public Boolean doInTransaction(TransactionStatus transactionStatus) {

				boolean blRetVal = false;

				try {
					// 기존 회원 존재 여부
					if (memberDAO.hasMemberByFld("ID", strId) == false) {
						throw new Exception("삭제할 회원정보가 존재하지 않습니다");
					}
					// 선택된 식단 삭제
		            chosenFoodMenuDAO.deleteChosenFoodMenuById(strId);

					if ( memberDAO.deleteRoles(strId) == true && memberDAO.deleteMemberById(strId) == true) {
						blRetVal = true;
					}
				} catch (Exception ex) {
					log.error("[MemberService][deleteMember] Exception: {}", ex);
					transactionStatus.setRollbackOnly();
				}

				return blRetVal;
			}
		});
	}

	// 회원정보 중복 점검(회원 가입)
	@Transactional(readOnly = true)
	@Override
	public boolean hasMemberByFld(String strField, String strValue) {

		return memberDAO.hasMemberByFld(strField, strValue);

	}

	// 회원정보 중복 점검(수정)
	@Transactional(readOnly = true)
	@Override
	public boolean hasMemberForUpdate(String strId, String strField, String strValue) {

		return memberDAO.hasMemberForUpdate(strId, strField, strValue);
	}

	// 회원 role 생성
	@Transactional
	@Override
	public boolean insertRole(Role role) {

		boolean blRetVal = false;

		try {
			memberDAO.insertRole(role.getUsernum(), role.getUserid(), role.getRole());
			blRetVal = true;
		} catch (Exception ex) {
			log.error("[MemberService][insertRole]: {}", ex);
			ex.printStackTrace();
		}
		return blRetVal;
	}

	// 회원 role 수정(관리자 권한)
	@Override
	public boolean updateRoles(String strId, boolean blRoleUserYn, boolean blRoleAdminYn) {

		boolean blRetVal = false;

		List<String> roles = memberDAO.selectRolesById(strId);
		// 회원(ROLE_USER)이면서 관리자 권한이 없는 경우
		if (blRoleAdminYn == false && roles.contains("ROLE_USER") == true
								   && roles.contains("ROLE_ABMIN") == false) {
				log.info("관리자 권한 할당");

				Role role = new Role();
				role.setUserid(strId);
				role.setRole("ROLE_ADMIN");

				blRetVal = this.insertRole(role);
		}
		// 회원(ROLE_USER)이면서 관리자 권한을 회수할 경우(관리자 권한 삭제)
		else if (blRoleAdminYn == false && roles.contains("ROLE_USER") == true
										&& roles.contains("ROLE_ABMIN") == true) {
			log.info("관리자 권한 회수");

			String role = "ROLE_ADMIN";
			blRetVal = this.deleteRoleById(strId, role);
		}
		return blRetVal;
	}

	// 회원 role 삭제
	@Transactional
	@Override
	public boolean deleteRoleById(String strId, String strRole) {

		boolean blRetVal = false;

		try {
			memberDAO.deleteRoleById(strId, strRole);
			blRetVal = true;

		} catch (Exception ex) {
			log.error("[MemberService][deleteRoleById] : {}", ex);
			ex.printStackTrace();
		}
		return blRetVal;
	}

	// 회원 enabled 상태 변경
	@Transactional
	@Override
	public boolean changeEnabled(String strId, int intEnabled) {

		boolean blRetVal = false;

		try {
			memberDAO.changeEnabled(strId, intEnabled);
			blRetVal = true;

		} catch (Exception ex) {
			log.error("[MemberServic][changeEnabled]: {}", ex);
			ex.printStackTrace();
		}
		return blRetVal;
	}

	// 페이징에 의해(페이지 별) 회원정보 조회(검색)
	@Transactional
	@Override
	public List<MemberVO> selectMembersByPaging(int intPage, int intLimit) {

		return memberDAO.selectMembersByPaging(intPage, intLimit);
	}

	// 전체 회원정보 조회
	@Transactional
	@Override
	public List<MemberVO> selectAllMembers(){

		return memberDAO.selectAllMembers();
	}

	// 회원정보 검색(페어링)
	@Transactional
	@Override
	public List<Map<String, Object>> selectMembersBySearchingAndPaging(String strSearchKey,String strSearchWord, int intPage, int intLimit, String strIsLikeOrEquals,
																	   String strOrdering ){

		return memberDAO.selectMembersBySearchingAndPaging(strSearchKey, strSearchWord, intPage, intLimit, strIsLikeOrEquals,
														   strOrdering);

	}

	// 전체 회원수 조회
	@Transactional
	@Override
	public int selectCountAll() {

		return memberDAO.selectCountAll();
	}

	// 검색된 총 회원정보 수 조회
	public int selectCountBySearching(String strSearchKey, String strSearchWord) {

		return memberDAO.selectCountBySearching(strSearchKey, strSearchWord);
	}

	// social(naver, google) 회원정보 저장
	@Transactional
	@Override
	public boolean insertSocialUser(SocialUser socialUser) {

		boolean blRetVal = false;

		try {
			socialUserMybatisDAO.insertSocialUser(socialUser);
			blRetVal = true;
		} catch (Exception ex) {
			log.error("[MemberService][insertSocialUser]: {}", ex);
			ex.printStackTrace();
		}

		return blRetVal;
	}

	// social (google) 회원정보 수정
	@Transactional
	@Override
	public boolean updateSocialUser(SocialUser socialUser) {
		boolean blRetVal = false;

		try {
			socialUserMybatisDAO.updateSocialUser(socialUser);
			blRetVal = true;
		} catch (Exception ex) {
			log.error("[MemberService][updateSocialUser]: {}", ex);
			ex.printStackTrace();
		}

		return blRetVal;
	}

	// 전체 회원정보 조회(json 작성용)
	@Transactional(readOnly = true)
	@Override
	public List<MemberJsonVO> selectAllMembersJson() {
		List<MemberJsonVO> members = new ArrayList<>();

		// 자체 회원정보 (비밀번호 제외)
		members.addAll(this.selectAllMembers().stream().map(x -> MemberJsonVO.toEntity(x)).toList());

		log.info("자체 회원 현황");
		members.forEach(x -> { log.info(x + ""); });

		// social 회원 정보
		members.addAll(this.selectAllSocialUsers().stream().map(x -> MemberJsonVO.toEntity(x)).toList());

		// 자체 회원 + social 회원
		log.info("자체 + social 회원 현황");
		members.forEach(x -> { log.info(x + ""); });

		return members;
	}

	// social(naver, google) 전체 회원정보 조회(ex, json 정보 생성용)
	@Transactional(readOnly = true)
	@Override
	public List<SocialUser> selectAllSocialUsers() {

		return socialUserMybatisDAO.selectAllSocialUsers();
	}

	// social (google) 회원정보 삭제(탈퇴)
	@Transactional
	@Override
	public boolean deletSocialUser(SocialUser socialUser) {
		boolean blRetVal = false;

		try {
			socialUserMybatisDAO.deletSocialUser(socialUser);
			blRetVal = true;

		} catch (Exception ex) {
			log.error("[MemberService][deletSocialUser] : {}", ex);
			ex.printStackTrace();
		}
		return blRetVal;
	}

//	 // 회원 아이디를 선택된 식단 데이터베이스에 추가
//	@Override
//	public boolean insertIdChosenFoodMenu(String strId) {
//		boolean blRetVal = false;
//
//		try {
//			chosenFoodMenuDAO.insertIdChosenFoodMenu(strId);
//			log.info("회원 아이디를 chosenFoodMenu 테이블에 저장");
//
//			blRetVal = true;
//
//		} catch (Exception ex) {
//			log.error("[MemberService][insertIdChosenFoodMenu] Exception: {}", ex);
//		}
//
//		return blRetVal;
//	}

	// 사용자별로 바뀔 식단 업데이트
	@Override
	public boolean updateFoodMenuByUser(String strId) {
		boolean blRetVal = false;

		// TODO FilePath 지정 필요
		String updatingFoodMenuByUsersFilePath = "";

		try {
			// updatingFoodMenuByUsers.json 파일에서 음식 메뉴 읽기
			JsonService jsonService = new JsonService();
			String updateFoodMenuJson = jsonService.readUpdateFoodMenuJson(updatingFoodMenuByUsersFilePath);
			log.info("[updateFoodMenuByUser][updateFoodMenuJson]");

			MemberVO memberVO = new MemberVO();
			memberVO.setId(strId);
			memberVO.setFoodmenu(updateFoodMenuJson);

			blRetVal = memberDAO.updateFoodMenuByUser(memberVO);

		} catch (IOException ex) {
			log.error("[updateFoodMenuByUser]IOException: {}", ex);

		} catch (Exception ex) {
			log.error("[updateFoodMenuByUser]Exception: {}", ex);
		}

		return blRetVal;

	}

	// 회원 아이디별로 바뀔 식단 업데이트
	@Override
	public boolean updateFoodMenuByUserID() {
		boolean blRetVal = false;

		// TODO FilePath 지정 필요
		String updatingFoodMenuByUsersFilePath = "";

		try {
			JsonService jsonService = new JsonService();

			// json 파일 읽기
			String jsonContent = jsonService.readUpdateFoodMenuJson(updatingFoodMenuByUsersFilePath);

			JSONArray jsonArray = new JSONArray(jsonContent);

			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String strId = jsonObject.getString("id");

				// 각 아이디에 대해 updateFoodMenuByUser 호출
				boolean result = updateFoodMenuByUser(strId);
				if (!result) {
					log.info("[updateFoodMenuByUserID][result] strId: {}", strId);
				}
			}
			// 모든 업데이트가 완료되면 true
			blRetVal = true;

			// 파일 삭제
			File fileToDelete = new File(updatingFoodMenuByUsersFilePath);
			if (fileToDelete.delete()) {
				log.info("[updateFoodMenuByUserID][updatingFoodMenuByUsersFile 삭제 성공]");
			} else {
				log.info("[updateFoodMenuByUserID][updatingFoodMenuByUsersFile 삭제 실패]");
			}

		} catch(IOException ex) {
			log.error("[updateFoodMenuByUserID] IOException: {}", ex);
		} catch(Exception ex) {
			log.error("[updateFoodMenuByUserID] Exception: {}", ex);
		}

		return blRetVal;
	}


}