package com.javateam.healthyFoodProject.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;
import lombok.extern.slf4j.Slf4j;
@SpringBootTest
@Slf4j
class ChosenFoodMenuDAOTest {
	@Autowired
	ChosenFoodMenuDAO chosenFoodMenuDAO;

	@Test
	void testSelectAllFoodMenu() {
		List<ChosenFoodMenuVO> list = chosenFoodMenuDAO.selectAllFoodMenu();
		assertEquals(2, list.size());
	}

	@Test
	void testSelectAllFoodMenu2() {

		ArrayList<String> updatingStrId = new ArrayList<String>();

		updatingStrId.add("abcd6666");
		String strFoodMenu = "";
		String strFoodMenuResult = "";

		List<ChosenFoodMenuVO> chosenFoodMenus = chosenFoodMenuDAO.selectAllFoodMenu();

		for (int i=0; i < chosenFoodMenus.size(); i++) {
			log.info("[saveChosenFoodMenuJson][chosenFoodMenus.get(i)]: {}", chosenFoodMenus.get(i));
			log.info("[saveChosenFoodMenuJson][chosenFoodMenus.get(i).getId() type]: {}", chosenFoodMenus.get(i).getId()==null? "null입니다." : chosenFoodMenus.get(i).getId().getClass().getName());
			log.info("[saveChosenFoodMenuJson][chosenFoodMenus.get(i).getFoodmenu() type]: {}", chosenFoodMenus.get(i).getFoodmenu()==null? "null입니다." : chosenFoodMenus.get(i).getFoodmenu().getClass().getName());
			log.info("[saveChosenFoodMenuJson][chosenFoodMenus.get(i).getFoodmenuResult() type]: {}", chosenFoodMenus.get(i).getFoodmenuResult()==null? "null입니다." : chosenFoodMenus.get(i).getFoodmenuResult().getClass().getName());

			if(!updatingStrId.contains(chosenFoodMenus.get(i).getId())) {
				strFoodMenu = chosenFoodMenus.get(i).getFoodmenu();
				strFoodMenuResult = chosenFoodMenus.get(i).getFoodmenuResult();
			}
		}
	}

}
