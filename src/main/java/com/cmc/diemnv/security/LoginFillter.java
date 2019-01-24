package com.cmc.diemnv.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cmc.diemnv.common.UserConstant;

public class LoginFillter {
	private LoginFillter() {
	}

	public static boolean isLogined(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute(UserConstant.ACCOUNT_SAVED) != null) {
			return true;
		} else {
			Cookie cookie[] = request.getCookies();
			if (cookie != null) {
				for (Cookie item : cookie) {
					if (item.getName().equals(UserConstant.ACCOUNT_SAVED)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
