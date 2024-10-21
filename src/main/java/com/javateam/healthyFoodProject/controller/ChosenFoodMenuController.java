package com.javateam.healthyFoodProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.javateam.healthyFoodProject.service.ChosenFoodMenuService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChosenFoodMenuController {

	@Autowired
	private ChosenFoodMenuService chosenFoodMenuService;

}
