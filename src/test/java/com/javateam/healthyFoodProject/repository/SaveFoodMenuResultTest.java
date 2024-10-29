package com.javateam.healthyFoodProject.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;
import com.javateam.healthyFoodProject.service.ChosenFoodMenuService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class SaveFoodMenuResultTest {

	@Autowired
	ChosenFoodMenuService chosenFoodMenuService;

	@Test
	void test() {
		ChosenFoodMenuVO chosenFoodMenuVO = chosenFoodMenuService.selectChosenFoodMenuById("abcd1111");
		log.info("[SaveFoodMenuResultTest][chosenFoodMenuVO]: {}", chosenFoodMenuVO);

	}

}
