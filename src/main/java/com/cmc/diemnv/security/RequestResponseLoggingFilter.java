package com.cmc.diemnv.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.cmc.diemnv.common.UserConstant;

public class RequestResponseLoggingFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request2 = (HttpServletRequest) request;
		if (isServletRequest(request2)) {
			String loginPath = request2.getServletPath();
			if (loginPath.equalsIgnoreCase("/login")) {
				chain.doFilter(request, response);
			} else {
				HttpSession session = request2.getSession();
				if (session.getAttribute(UserConstant.ACCOUNT_SAVED) != null) {
					chain.doFilter(request, response);
				} else {
					Cookie cookie[] = request2.getCookies();
					boolean isExist = false;
					if (cookie != null) {
						for (Cookie cookie2 : cookie) {
							if (UserConstant.ACCOUNT_SAVED.equals(cookie2)) {
								isExist = true;
								break;
							}
						}
					}
					if (isExist) {
						chain.doFilter(request, response);
					} else {
						HttpServletResponse response2 = (HttpServletResponse) response;
						response2.sendRedirect(request2.getContextPath() + "/login");
					}
				}
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	private boolean isServletRequest(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		String pathInfo = request.getPathInfo();
		String url = servletPath;
		if (pathInfo != null) {
			url += "/*";
		}

		Map<String, ? extends ServletRegistration> servletRegistrantions = request.getServletContext()
				.getServletRegistrations();
		Collection<? extends ServletRegistration> values = servletRegistrantions.values();
		for (ServletRegistration servletRegistration : values) {
			Collection<String> urlCollection = servletRegistration.getMappings();
			for (String item : urlCollection) {
				if (item.equals(url)) {
					return true;
				}
			}
		}
		return false;
	}

}
