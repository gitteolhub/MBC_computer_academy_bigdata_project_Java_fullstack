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

	// social(naver, google) 회원정보 저장
	public void insertSocialUser(SocialUser socialUser) {
		sqlSession.insert("mapper.healthyFoodMapper.insertSocialUser", socialUser);
	}

	// social (google) 회원정보 수정
	public void updateSocialUser(SocialUser socialUser) {
		sqlSession.update("mapper.healthyFoodMapper.updateSocialUser", socialUser);
	}

	// social(naver, google) 전체 회원 조회
	public List<SocialUser> selectAllSocialUsers() {
		return sqlSession.selectList("mapper.healthyFoodMapper.selectAllSocialUsers");
	}

	// social (google) 회원정보 삭제
	public void deletSocialUser(SocialUser socialUser) {
		sqlSession.delete("mapper.healthyFoodMapper.deleteSocialUser", socialUser);
	}
}
