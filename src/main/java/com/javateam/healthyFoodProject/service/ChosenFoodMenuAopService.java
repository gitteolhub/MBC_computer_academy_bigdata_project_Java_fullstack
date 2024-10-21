package com.javateam.healthyFoodProject.service;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Service
@Slf4j
public class ChosenFoodMenuAopService {

	@Autowired
	JsonService jsonService;

	/**
     * ChosenFoodMenuDAO 호출(insertIdChosenFoodMenu, insertChosenFoodMenu, updateChosenFoodMenu, deleteChosenFoodMenuById)
     * 이후 자동 실행
     * ex) 주의 selectAllFoodMenu 제외 (StackOverflowError 에러 발생)
     */
	@Pointcut("execution(public * com.javateam.healthyFoodProject.repository.ChosenFoodMenuDAO.*ChosenFoodMenu*(..))")
	public void pointcutChosenFoodMenuSave() {
		log.info("[pointcutChosenFoodMenuSave]");
	}

	/**
     * ChosenFoodMenuDAO 호출 이후 JSON 파일 저장
     */
	@After("pointcutChosenFoodMenuSave()")
	public void chosenFoodMenuJsonSaveAfterAdvice() {
		log.info("[chosenFoodMenuJsonSaveAfterAdvice]");

		jsonService.saveChosenFoodMenuJson();
	}
}
