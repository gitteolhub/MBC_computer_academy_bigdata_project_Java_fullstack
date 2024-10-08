package com.example.teamproject.repository;


import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.teamproject.domain.MemberVO;

// import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

@Repository
// @Slf4j
// @Log4j2
public class MemberDAOImpl implements MemberDAO {

	private static final Logger log
		= LoggerFactory.getLogger(MemberDAOImpl.class);

	@Autowired
	SqlSession sqlSession; // MyBatis Session 객체

	@Override
	public MemberVO selectMemberById(String id) {

		return sqlSession.selectOne("mapper.MemberMapper.selectMemberById", id);
	}
}