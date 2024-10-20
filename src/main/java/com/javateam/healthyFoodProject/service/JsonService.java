package com.javateam.healthyFoodProject.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;
import com.javateam.healthyFoodProject.domain.MemberJsonVO;
import com.javateam.healthyFoodProject.domain.MemberVO;
import com.javateam.healthyFoodProject.repository.ChosenFoodMenuDAO;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JsonService {

	@Autowired
	public MemberService memberService;

	@Autowired
	public ChosenFoodMenuDAO chosenFoodMenuDAO;

	public void saveMemberDataJson() {
		List<MemberJsonVO> allUserData = memberService.selectAllMembersJson();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// String filePath="D:/coding/work/JsonData";
		 String filePath="src/main/resources/JsonDataFiles/AllMembersDump.json";

		try{
			String json = gson.toJson(allUserData);
			Files.write(Paths.get(filePath), json.getBytes());
			log.info("[saveMemberDataJson]");

		} catch (IOException ex) {
			log.error("[saveMemberDataJson] IOException");
			ex.printStackTrace();
		}
	}

	// 초기식단 저장 파일을 읽어서 반환하는 함수
	public String readFoodMenuJson(String filePath) throws IOException {
		String strRetVal;
		String strJson = "";

		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String str;
		while((str = bufferedReader.readLine()) != null) {
			log.info("[readFoodMenuJson][str]: {}", str);

			strJson += str;
		}
		strRetVal = strJson;
		return strRetVal;
	}

	public String readUpdateFoodMenuJson(String filePath) throws IOException {
		return readFoodMenuJson(filePath);
	}

	// 선택된 식단 전체 json 파일로 저장
	public void saveChosenFoodMenuJson() {
		List<ChosenFoodMenuVO> chosenFoodMenus = chosenFoodMenuDAO.selectAllFoodMenu();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String chosenFoodMenuFilePath = "src/main/resources/JsonDataFiles/chosenFoodMenu_Json.json";

		try {
			String json = gson.toJson(chosenFoodMenus);
			Files.write(Paths.get(chosenFoodMenuFilePath), json.getBytes());
			log.info("[saveChosenFoodMenuJson]: {}", chosenFoodMenuFilePath);

		} catch (IOException ex){
			log.error("[saveChosenFoodMenuJson] IOException: {}", ex);
		}

	}

}
