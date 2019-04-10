package couponsystem.services.restImplementations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import couponsystem.entities.Client;
import couponsystem.entities.Company;
import couponsystem.entities.Coupon;
import couponsystem.entities.Customer;
import couponsystem.entities.Income;
import couponsystem.enums.ClientType;
import couponsystem.exeptions.CouponSystemException;
import couponsystem.repositories.CompanyRepository;
import couponsystem.repositories.CustomerRepository;
import couponsystem.repositories.IncomeRepository;
import couponsystem.services.AdminService;
import couponsystem.utilities.CouponSystemUtils;


@RestController
@RequestMapping("secure/admin")
public class AdminServiceImplementation implements AdminService {

	public static final int MINIMUM_USERNAME_LENGTH = 3;
	public static final int MAXIMUM_USERNAME_LENGTH = 25;
	private static final int MINIMUM_PASSWORD_LENGTH = 4;
	private static final int MAXIMUM_PASSWORD_LENGTH = 20;
	private static final int MINIMUM_EMAIL_LENGTH = 7;
	private static final int MAXIMUM_EMAIL_LENGTH = 50;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private IncomeRepository incomeRepository;

	
	@Override
	@PostMapping("create-company")
	public long createCompany(@RequestBody Company company) throws CouponSystemException {

		// checking for nulls and illegal lengths in fields. An exception will be thrown
		// in case of an invalid argument
		validateName(company.getName(), ClientType.Company);
		validatePassword(company.getPassword());
		validateEmail(company.getEmail());

		company = companyRepository.save(company);

		System.out.println("Company \"" + company.getName() + "\" created successfully.");

		return company.getId();
	}

	@Override
	@Transactional
	@DeleteMapping("remove-company/{id}")
	public boolean removeCompany(@PathVariable long id) throws CouponSystemException {
		
		List<Customer> customers = customerRepository.findAll();
		for (Customer customer : customers) {
			List<Coupon> coupons = customer.getCoupons();
			for (Coupon coupon : coupons) {
				if (coupon.getCompany().getId() == id) {
					throw new CouponSystemException("Too late to remove company : coupons that belong to company have been purchased by customers", HttpStatus.FORBIDDEN);
				}
			}
		}
		
		//This line will throw an exception if the company is not found and therefore cannot be deleted
		Company company = uploadCompanyFromDatabase(id);
		
		companyRepository.deleteById(id);
		
		if (companyRepository.findById(id) != null) {
			throw new CouponSystemException("Error removing company " + company.getName()); 
		}
		
		System.out.println("Company " + company.getName() + " removed successfully");
		
		return true;
	}

	@Override
	@PutMapping("update-company")
	public boolean updateCompany(@RequestBody Company company) throws CouponSystemException {
		
		Company existingCompany = uploadCompanyFromDatabase(company.getId());
		
		if ( !(company.getName().equals(existingCompany.getName())) ) {
			validateName(company.getName(), ClientType.Company);
		}
		validateEmail(company.getEmail());
		
		if ( company.getPassword().equals("Classified") ) {
			company.setPassword(existingCompany.getPassword());
		} else {
			//This code will only be reached if the company changed it's password
			validatePassword(company.getPassword());
		}
		
		companyRepository.save(company);
		return true;
	}

	@Override
	@GetMapping("get-company/{id}")
	public Company getCompany(@PathVariable long id) throws CouponSystemException {
		Company company = uploadCompanyFromDatabase(id);
		CouponSystemUtils.classifyPassword(company);
		
		return company;
	}

	@Override
	@GetMapping("get-all-companies")
	public Collection<Company> getAllCompanies() throws CouponSystemException {
		List<Company> allCompanies = companyRepository.findAll();
		
		for (Company company : allCompanies) {
			CouponSystemUtils.classifyPassword(company);
		}
		
		return allCompanies;
	}

	@Override
	@PostMapping("create-customer")
	public long createCustomer(@RequestBody Customer customer) throws CouponSystemException {
		
		validateName(customer.getName(), ClientType.Customer);
		validatePassword(customer.getPassword());
		
		customer = customerRepository.save(customer);
		
		System.out.println("Customer " + customer.getName() + " created successfully");
		
		return customer.getId();
	}

	@Override
	@Transactional
	@DeleteMapping("remove-customer/{id}")
	public boolean removeCustomer(@PathVariable long id) throws CouponSystemException {
		
		Customer customer = uploadCustomerFromDatabase(id);
		
		customerRepository.deleteById(id);
		
		if (customerRepository.findById(id) != null) {
			throw new CouponSystemException("Error removing customer " + customer.getName());
		}
		
		return true;
	}

	@Override
	@PutMapping("update-customer")
	public boolean updateCustomer(@RequestBody Customer customer) throws CouponSystemException {
		
		Customer existingCustomer = uploadCustomerFromDatabase(customer.getId());
		
		if ( !(customer.getName().equals(existingCustomer.getName())) ) {
			validateName(customer.getName(), ClientType.Customer);
		}
		
		if ( customer.getPassword().equals("Classified") ) {
			customer.setPassword(existingCustomer.getPassword());
		} else {
			//This code will only be reached if the customer changed their password
			validatePassword(customer.getPassword());
		}
		
		customerRepository.save(customer);
		
		return true;
	}

	@Override
	@GetMapping("get-customer/{id}")
	public Customer getCustomer(@PathVariable long id) throws CouponSystemException {
		
		Customer customer = uploadCustomerFromDatabase(id);
		CouponSystemUtils.classifyPassword(customer);
		
		return customer;
	}

	@Override
	@GetMapping("get-all-customers")
	public Collection<Customer> getAllCustomers() {
		
		List<Customer> customers = customerRepository.findAll();
		
		for (Customer customer : customers) {
			CouponSystemUtils.classifyPassword(customer);
		}
		
		return customers;
	}

	@Override
	@GetMapping("get-all-income")
	public Collection<Income> getAllIncome() {
		return incomeRepository.findAll();
	}

	@Override
	@GetMapping("get-income-sum")
	public double getIncomeSum() {
		Collection<Income> allIncome = incomeRepository.findAll();
		double sum = 0;
		for (Income income : allIncome) {
			sum += income.getAmount();
		}
		return sum;
	}

	@Override
	@GetMapping("get-income-by-name/{name}")
	public Collection<Income> getIncomeByName(@PathVariable String name) {
		return incomeRepository.findByName(name);
	}

	// ALL VALIDATIONS:

	private void validateName(String name, ClientType clientType) throws CouponSystemException {

		// Checking for invalid length
		if (name == null || !CouponSystemUtils.between(name.length(), MINIMUM_USERNAME_LENGTH, MAXIMUM_USERNAME_LENGTH)) {

			throw new CouponSystemException("Username must contain " + MINIMUM_USERNAME_LENGTH + " to "
					+ MAXIMUM_USERNAME_LENGTH + " characters, and must not be empty.");
		}

		// Checking name availability
		List<Client> clients = new ArrayList<>();

		switch (clientType) {
		case Company:
			clients.addAll(companyRepository.findAll());
			break;
		case Customer:
			clients.addAll(customerRepository.findAll());
			break;
		default:
			throw new CouponSystemException("The client type " + clientType + " is not supported.");
		}

		for (Client existingClient : clients) {
			if (existingClient.getName().equals(name)) {
				throw new CouponSystemException("Failed to create " + clientType.toString().toLowerCase()
						+ ", the name " + name + " is already in use.");
			}
		}

		System.out.println("The name " + name + " is available and ready for the new client to use.");
	}

	private void validatePassword(String password) throws CouponSystemException {

		if (password == null || !CouponSystemUtils.between(password.length(), MINIMUM_PASSWORD_LENGTH, MAXIMUM_PASSWORD_LENGTH)) {

			throw new CouponSystemException("Password must contain " + MINIMUM_PASSWORD_LENGTH + " to "
					+ MAXIMUM_PASSWORD_LENGTH + " characters, and must not be empty.");
		}
	}

	private void validateEmail(String email) throws CouponSystemException {

		if (email == null || !CouponSystemUtils.between(email.length(), MINIMUM_EMAIL_LENGTH, MAXIMUM_EMAIL_LENGTH)) {

			throw new CouponSystemException("Email must contain " + MINIMUM_EMAIL_LENGTH + " to "
					+ MAXIMUM_EMAIL_LENGTH + " characters, and must not be empty.");
		}
	}
	
	private Company uploadCompanyFromDatabase(long id) throws CouponSystemException {
		Company company = companyRepository.findById(id);
		
		if (company == null) {
			throw new CouponSystemException("Company not found : Could not find company with id " + id, HttpStatus.NOT_FOUND);
		}
		
		return company;
	}
	
	private Customer uploadCustomerFromDatabase(long id) throws CouponSystemException {
		Customer customer = customerRepository.findById(id);
		
		if (customer == null) {
			throw new CouponSystemException("Customer not found : Could not find customer with id " + id, HttpStatus.NOT_FOUND);
		}
		
		return customer;
	}
	

}
