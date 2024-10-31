package com.javateam.healthyFoodProject.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javateam.healthyFoodProject.domain.ChosenFoodMenuVO;
import com.javateam.healthyFoodProject.domain.MemberJsonVO;
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

	public ArrayList<String> updatingStrId = new ArrayList<String>();

	public void saveMemberDataJson() {
		List<MemberJsonVO> allUserData = memberService.selectAllMembersJson();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
	public void saveChosenFoodMenuJson(String strId) {
		updatingStrId.add(strId);
		String strFoodMenu = "";
		String strFoodMenuResult = "";

		List<ChosenFoodMenuVO> chosenFoodMenus = chosenFoodMenuDAO.selectAllFoodMenu();

		for (int i=0; i < chosenFoodMenus.size(); i++) {
			if(!updatingStrId.contains(chosenFoodMenus.get(i).getId())) {
				strFoodMenu = chosenFoodMenus.get(i).getFoodmenu();
				strFoodMenuResult = chosenFoodMenus.get(i).getFoodmenuResult();
			}
		}

		String chosenFoodMenuFilePath = "src/main/resources/JsonDataFiles/chosenFoodMenu_Json.json";

		 File file = new File(chosenFoodMenuFilePath);
		 try {
			 FileWriter fileWriter = new FileWriter(file, true);
			 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			 String strWriting = "";

			 if(file.createNewFile()) {
				 log.info("[chosenFoodMenu_Json 파일이 없어 새로 만들었습니다.]");

				 strWriting = "{";

			 } else {
				 log.info("[chosenFoodMenu_Json 파일이 있습니다.]");
				 strWriting = ",{";
			 }
			 strWriting += strId + ":";
			 strWriting += "[" + strFoodMenu + "]|";
			 strWriting += strFoodMenuResult + "}";

			 bufferedWriter.write(strWriting);
			 bufferedWriter.close();

		 } catch(IOException ex) {
			log.error("[saveChosenFoodMenuJson][IOException]: {}", ex);
		 }

	}

}
