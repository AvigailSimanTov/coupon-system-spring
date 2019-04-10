package couponsystem.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import couponsystem.entities.Company;
import couponsystem.entities.Customer;
import couponsystem.services.restImplementations.LoginServiceImplementation;

@WebFilter("/secure/*")
public class LoginFilter implements Filter {

	private HttpSession session;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		session = httpRequest.getSession(false);
		String requestUri = httpRequest.getRequestURI();

		if (session == null) {
			System.out.println("Not logged in : session is null");
			return;
		}

		System.out.println("[filter] URI " + requestUri);
		Object user = session.getAttribute("user");

		if (user == null) {
			System.out.println("Not logged in : session user is null");
			return;
		}

		if (requestUri.contains("/admin") && !user.equals(LoginServiceImplementation.ADMIN_USERNAME)) {
			System.out.println("Not logged in as client type 'Admin' : access denied");
			return;
		} else if (requestUri.contains("/company") && !(user instanceof Company)) {
			System.out.println("Not logged in as client type 'Company' : access denied");
			return;
		} else if (requestUri.contains("/customer") && !(user instanceof Customer)) {
			System.out.println("Not logged in as client type 'Customer' : access denied");
			return;
		}

		System.out.println("[filter] session user: " + user);
		filterChain.doFilter(request, response);
	}

}
