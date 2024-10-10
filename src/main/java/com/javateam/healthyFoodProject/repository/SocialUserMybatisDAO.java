package com.javateam.healthyFoodProject.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javateam.healthyFoodProject.domain.SocialUser;

@Repository
public class SocialUserMybatisDAO {

	@Autowired
	SqlSession sqlSession;

	public void insertSocialUser(SocialUser socialUser) {
		sqlSession.insert("mapper.healthyFoodMapper.insertSocialUser", socialUser);
	}

	public void updateSocialUser(SocialUser socialUser) {
		sqlSession.update("mapper.healthyFoodMapper.updateSocialUser", socialUser);
	}
}
