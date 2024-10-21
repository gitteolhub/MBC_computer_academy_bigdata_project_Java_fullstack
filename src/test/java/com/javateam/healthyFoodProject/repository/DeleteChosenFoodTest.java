package com.javateam.healthyFoodProject.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class DeleteChosenFoodTest {

	@Autowired
	ChosenFoodMenuDAO chosenFoodMenuDAO;

	@Test
	void testSelectAllFoodMenu() {
		List<ChosenFoodMenuVO> list = chosenFoodMenuDAO.selectAllFoodMenu();
		assertEquals(3, list.size());
	}

	@Test
	void testDeleteChosenFoodMenuById() {
		String strId = "abcd5555";
        chosenFoodMenuDAO.deleteChosenFoodMenuById(strId);
	}

}
