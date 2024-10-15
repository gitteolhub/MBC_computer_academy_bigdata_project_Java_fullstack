package com.javateam.healthyFoodProject.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javateam.healthyFoodProject.domain.MemberJsonVO;
import com.javateam.healthyFoodProject.domain.MemberVO;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberJsonService {

	@Autowired
	public MemberService memberService;

	public void saveMemberDataJson() {
		List<MemberJsonVO> allUserData = memberService.selectAllMembersJson();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// String filePath="D:/coding/work/JsonData";
		 String filePath="D:/coding/work/JsonData/AllMembersDump.json";

		try{
			String json = gson.toJson(allUserData);
			Files.write(Paths.get(filePath), json.getBytes());
			log.info("[saveMemberDataJson]");

		} catch (IOException ex) {
			log.error("[saveMemberDataJson error]");
			ex.printStackTrace();
		}
	}

}
