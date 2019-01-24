package com.cmc.diemnv.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cmc.diemnv.common.UserConstant;
import com.cmc.diemnv.entity.Account;
import com.cmc.diemnv.entity.User;
import com.cmc.diemnv.security.LoginFillter;
import com.cmc.diemnv.service.impl.AccountServiceImpl;
import com.cmc.diemnv.service.impl.UserServiceImpl;

@Controller
public class UserController {

	@Autowired
	private UserServiceImpl UserService;

	@Autowired
	private AccountServiceImpl accountService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String viewLoginPage(Model model, HttpServletRequest request, HttpServletResponse response) {
		if (LoginFillter.isLogined(request)) {
			return "redirect:/home";
		}
		model.addAttribute("account", new Account());
		return "common/login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String viewLoginPage(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.setAttribute(UserConstant.ACCOUNT_SAVED, null);
		Cookie cookie = new Cookie(UserConstant.ACCOUNT_SAVED, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		return "redirect:/login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String viewSignupPage(Model model, HttpServletRequest request) {
		if (LoginFillter.isLogined(request)) {
			model.addAttribute("user", new User());
			return "common/register";
		}
		return "redirect:/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String actionLogin(Model model, @ModelAttribute Account account, RedirectAttributes r,
			HttpServletRequest request, HttpServletResponse response) {
		if (accountService.login(account)) {
			HttpSession session = request.getSession();
			session.setAttribute(UserConstant.ACCOUNT_SAVED, "isLogined");
			if (request.getParameter("remember") != null) {
				Cookie cookie = new Cookie(UserConstant.ACCOUNT_SAVED, "isLogined");
				cookie.setMaxAge(24 * 60 * 60);
				response.addCookie(cookie);
			}
			return "redirect:/home";
		} else {
			model.addAttribute("loginError", true);
			model.addAttribute("user", account);
			return "common/login";
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String actionRegister(Model model, @ModelAttribute User user, RedirectAttributes r,
			HttpServletRequest request) {
		if (LoginFillter.isLogined(request)) {
			boolean insertSuccess = UserService.insertUser(user, model);
			if (insertSuccess) {
				r.addFlashAttribute("userCreated", user.getFirstName() + " " + user.getLastName());
				return "redirect:/";
			} else {
				return "common/register";
			}
		}
		return "redirect:/login";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteUser(@RequestParam("id") String id, Model model, HttpServletRequest request) {
		if (LoginFillter.isLogined(request)) {
			Long id2 = Long.parseLong(id);
			User user = UserService.getUserById(id2);
			UserService.deleteUser(user);
			return "redirect:/";
		}
		return "redirect:/login";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String editUser(@RequestParam("id") String id, Model model, HttpServletRequest request) {
		if (LoginFillter.isLogined(request)) {
			Long id2 = Long.parseLong(id);
			User user = UserService.getUserById(id2);
			model.addAttribute("user", user);
			return "common/edit";
		}
		return "redirect:/login";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editUser(@ModelAttribute User user, HttpServletRequest request) {
		if (LoginFillter.isLogined(request)) {
			if (UserService.updateUser(user)) {
				return "redirect:/home";
			} else {
				return "common/404";
			}
		}
		return "redirect:/login";
	}

	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public String findUser(@RequestParam("sequence") String sequence, Model model) {
		List<User> list = UserService.getUserListBySequence(sequence, 0, Integer.MAX_VALUE);
		model.addAttribute("users", list);
		model.addAttribute("sequence", sequence);
		return "common/index";
	}
}
