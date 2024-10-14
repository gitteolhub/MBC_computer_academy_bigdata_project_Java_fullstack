package com.javateam.healthyFoodProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javateam.healthyFoodProject.service.MemberService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("member")
@Slf4j
public class MemberRestController {

	@Autowired
	public MemberService memberService;

	@GetMapping("/hasFld/{fld}/{val}")
	public ResponseEntity<Boolean>hasFld(@Parameter(name="fld", required = true) @PathVariable("fld") String strField,
										 @Parameter(name="val", required = true) @PathVariable("val") String strValue) {

		ResponseEntity<Boolean> responseEntity = null;

		try {
			boolean blRetVal = memberService.hasMemberByFld(strField, strValue);

			if(blRetVal == true) {
				// 중복 값(아이디)이 있을 경우
				// http 상태 코드 200 : OK (중복 : 사용불가능)
				responseEntity = new ResponseEntity<>(blRetVal, HttpStatus.OK);

			} else {
				// 중복 값(아이디)이 없을 경우
				// http 상태 코드 204 : NO Content (중복안됨 : 사용가능)
				responseEntity = new ResponseEntity<>(blRetVal,HttpStatus.NO_CONTENT);
			}
		} catch (Exception ex) {
			log.error("[MemberRestController][hasFld] Exception: {}", ex);

			// http 상태 코드 500 : Internal Server Error (서버 에러)
			responseEntity= new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}

	@GetMapping("/hasFldForUpdate/{id}/{fld}/{val}")
	public ResponseEntity<Boolean>hasFldForUpdate (@Parameter(name = "id",  required = true) @PathVariable("id")  String strId,
												   @Parameter(name = "fld", required = true) @PathVariable("fld") String strField,
												   @Parameter(name = "val", required = true) @PathVariable("val") String strValue){
		ResponseEntity responseEntity = null;
		try {
			boolean blRetVal = memberService.hasMemberForUpdate(strId, strField, strValue);

			if(blRetVal == true) {
				// 중복 값(이메일)이 있을 경우
				// http 상태 코드 200 : OK (중복 : 사용불가능)
				responseEntity = new ResponseEntity<>(blRetVal, HttpStatus.OK);

			} else {
				// 증복 값(이메일)이 없을 경우
				// http 상태 코드 204 : NO Content (중복안됨 : 사용가능)
				responseEntity = new ResponseEntity<>(blRetVal, HttpStatus.NO_CONTENT);
			}
		} catch (Exception ex) {
			log.error("[MemberRestController][hasFldForUpdate] Exception: {}", ex);

			// http 상태 코드 500 : Internal Server Error (서버 에러)
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return responseEntity;
	}

}
