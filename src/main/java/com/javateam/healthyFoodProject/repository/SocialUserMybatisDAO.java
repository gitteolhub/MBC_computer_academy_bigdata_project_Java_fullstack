package com.javateam.healthyFoodProject.repository;

import java.util.List;

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

	// social (google) 회원정보 삭제
	public void deletSocialUser(SocialUser socialUser) {
		sqlSession.delete(MAPPER_PATH + "deleteSocialUser", socialUser);
	}
}
