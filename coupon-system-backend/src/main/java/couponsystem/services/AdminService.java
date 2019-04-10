package couponsystem.services;

import java.util.Collection;

import couponsystem.entities.Company;
import couponsystem.entities.Customer;
import couponsystem.entities.Income;
import couponsystem.exeptions.CouponSystemException;

public interface AdminService {

	//Company management:
	long createCompany(Company company) throws CouponSystemException;
	
	boolean removeCompany(long id) throws CouponSystemException;
	
	boolean updateCompany(Company company) throws CouponSystemException;
	
	Company getCompany(long id) throws CouponSystemException;
	
	Collection<Company> getAllCompanies() throws CouponSystemException;

	//Customer management:
	long createCustomer(Customer customer) throws CouponSystemException;
	
	boolean removeCustomer(long id) throws CouponSystemException;
	
	boolean updateCustomer(Customer customer) throws CouponSystemException;
	
	Customer getCustomer(long id) throws CouponSystemException;
	
	Collection<Customer> getAllCustomers();

	//Income management:
	Collection<Income> getAllIncome() throws CouponSystemException;

	double getIncomeSum() throws CouponSystemException;

	Collection<Income> getIncomeByName(String name) throws CouponSystemException;
	
}
