package com.javateam.healthyFoodProject.service;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Service
@Slf4j
public class MemberJsonSaveAopService {

	@Autowired
	JsonService jsonService;

    /**
     *  MemberService.insertMember, MemberService.updateMember, MemberService.deleteMember
     *  호출 이후 자동 실행
     */
	@Pointcut("execution(public * com.javateam.healthyFoodProject.service.MemberService.*Member(..))")
	public void pointcutMemberJsonSave() {
		log.info("[pointcutMemberJsonSave]");
	}

	/**
     * MemberService.insertSocialUser, MemberService.updateSocialUser, MemberService.deleteSocialUser
     *  호출 이후 자동 실행
     */
	@Pointcut("execution(public * com.javateam.healthyFoodProject.service.MemberService.*SocialUser(..))")
	public void pointcutSocialUserJsonSave() {
		log.info("[pointcutSocialUserJsonSave]");
	}

	/**
     * SocialUserDAO.insertSocialUser(CustomOAuth2UserService에서 호출 이후 자동 실행)
     */
	@Pointcut("execution(public * com.javateam.healthyFoodProject.repository.SocialUserMybatisDAO.insertSocialUser(..))")
	public void pointcutSocialUserJsonDAOSave() {
		log.info("[pointcutSocialUserJsonDAOSave]");
	}

	/**
	 * pointcutMemberJsonSave() 이거나(또는) pointcutSocialUserJsonSave()
	 * 이거나(또는) pointcutSocialUserJsonDAOSave 시점에서 실행(advice)
	 */
	@After("pointcutMemberJsonSave() || pointcutSocialUserJsonSave() || pointcutSocialUserJsonDAOSave()")
	public void memberJsonSaveAfterAdvice() {

		log.info("[memberJsonSaveAfterAdvice]");

		// 아래의 함수들이 호출될 때 마다 Json 파일("src/main/resources/JsonDataFiles/AllMembersDump.json") 자동 저장
		/*
		 * MemberService.insertMember
		   MemberService.updateMember
		   MemberService.deleteMember
		   MemberService.insertSocialUser
		   MemberService.updateSocialUser
		   MemberService.deleteSocialUser
		   SocialUserMybatisDAO.insertSocialUser
		 */

		jsonService.saveMemberDataJson();

	}


}
