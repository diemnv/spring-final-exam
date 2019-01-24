package com.cmc.diemnv.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmc.diemnv.security.LoginFillter;
import com.cmc.diemnv.service.impl.UserServiceImpl;

@Controller
public class HomeController {
	@Autowired
	private UserServiceImpl userService;

	@RequestMapping(value = { "/", "/home" })
	public String index(Model model, HttpServletRequest request) {
		if (LoginFillter.isLogined(request)) {
			model.addAttribute("logined", true);
		}
		model.addAttribute("users", userService.getAllUsers());
		return "common/index";
	}
}
