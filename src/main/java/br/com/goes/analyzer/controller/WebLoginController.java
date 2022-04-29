package br.com.goes.analyzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WebLoginController {

	//@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
}
