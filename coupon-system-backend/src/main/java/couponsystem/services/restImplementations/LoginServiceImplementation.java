package couponsystem.services.restImplementations;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import couponsystem.entities.Company;
import couponsystem.entities.Customer;
import couponsystem.enums.ClientType;
import couponsystem.exeptions.CouponSystemException;
import couponsystem.repositories.CompanyRepository;
import couponsystem.repositories.CustomerRepository;


@RestController
@RequestMapping("couponsystem")
public class LoginServiceImplementation {

	public static final Object ADMIN_USERNAME = "admin";

	private static final Object ADMIN_PASSWORD = "1234";

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	CustomerRepository customerRepository;
	
	@PostMapping("login/{clientType}")
	public boolean login(@PathVariable ClientType clientType, @RequestParam String username, @RequestParam String password, HttpSession session) throws CouponSystemException {
		
		String errorMessage = "Access denied : Wrong login details for client type " + clientType + ". Username '" + username + "' or password incorrect.";
		CouponSystemException wrongLoginDetails = new CouponSystemException(errorMessage);
		wrongLoginDetails.setHttpStatus(HttpStatus.FORBIDDEN);
		
		switch (clientType) {

		case Admin:
			if (!(username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD))) {
				throw wrongLoginDetails;
			}
			System.out.println("Admin logged in successfully.");
			session.setAttribute("user", ADMIN_USERNAME);
			return true;

		case Company:
			Company company = companyRepository.findByNameAndPassword(username, password);
			
			if (company == null) {
				throw wrongLoginDetails;
			}
			
			System.out.println("Company " + company.getName() + " logged in successfully.");
			session.setAttribute("user", company);
			
			return true;

		case Customer:
			Customer customer = customerRepository.findByNameAndPassword(username, password);
			
			if (customer == null) {
				throw wrongLoginDetails;
			}
			
			System.out.println("Customer " + customer.getName() + " logged in successfully.");
			session.setAttribute("user", customer);
			
			return true;
			
		default:
			return false;

		}
	}
	
	@RequestMapping("logout")
	public void logout(HttpSession session) {
		session.invalidate();
		System.out.println("Logged out");
	}
	
}
