package com.example.teamproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	@GetMapping("/join")
	public String join() {
		return "join";
	}
	@GetMapping("/member")
	public String member() {
		return "member";
	}

	@GetMapping("/adminPage")
	public String adminPage() {
		return "adminPage";
	}
	@GetMapping("/main")
	public String main() {
		return "main";
	}
}