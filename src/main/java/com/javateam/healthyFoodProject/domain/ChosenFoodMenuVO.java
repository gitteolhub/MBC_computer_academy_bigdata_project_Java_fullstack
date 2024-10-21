package com.javateam.healthyFoodProject.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ChosenFoodMenuVO {

	private String id;
	private String foodmenu;
	private String foodmenuResult;

}