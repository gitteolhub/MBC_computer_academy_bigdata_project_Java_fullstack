package com.javateam.healthyFoodProject.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.healthyFoodProject.domain.SocialUser;

@Repository
public class SocialUserMybatisDAO {

	@Autowired
	SqlSession sqlSession;

	private static final String MAPPER_PATH = "mapper.healthyFoodMapper.";

	// social(naver, google) 회원정보 저장
	public void insertSocialUser(SocialUser socialUser) {
		sqlSession.insert(MAPPER_PATH + "insertSocialUser", socialUser);
	}

	// social (google) 회원정보 수정
	public void updateSocialUser(SocialUser socialUser) {
		sqlSession.update(MAPPER_PATH + "updateSocialUser", socialUser);
	}

	// social(naver, google) 전체 회원 조회
	public List<SocialUser> selectAllSocialUsers() {
		return sqlSession.selectList(MAPPER_PATH + "selectAllSocialUsers");
	}

	// social(naver, google) 회원 조회(id로 조회)
	public SocialUser selectSocialUserById(int id) {
		return sqlSession.selectOne(MAPPER_PATH + "selectSocialUserById", id);
	}

	// social(naver, google) 회원 조회(email, authVendor로 조회)
	public SocialUser selectSocialUserByEmailAndAuthVendor(String email, String authVendor) {
		Map<String, String> map = new HashMap<>();
		map.put("email", email);
		map.put("authVendor", authVendor);

		return sqlSession.selectOne(MAPPER_PATH + "selectSocialUserByEmailAndAuthVendor", map);
	}

	// social (google) 회원정보 삭제
	public void deletSocialUser(SocialUser socialUser) {
		sqlSession.delete(MAPPER_PATH + "deleteSocialUser", socialUser);
	}


}
