package couponsystem.services.restImplementations;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import couponsystem.entities.Coupon;
import couponsystem.entities.Customer;
import couponsystem.entities.Income;
import couponsystem.enums.IncomeType;
import couponsystem.exeptions.CouponSystemException;
import couponsystem.repositories.CouponRepository;
import couponsystem.repositories.CustomerRepository;
import couponsystem.repositories.IncomeRepository;
import couponsystem.services.CustomerService;
import couponsystem.utilities.CouponSystemUtils;

@RestController
@RequestMapping("secure/customer")
public class CustomerServiceImplementation implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CouponRepository couponRepository;
	
	@Autowired
	private IncomeRepository incomeRepository;

	Customer customerFromSession;

	@Override
	@Transactional
	@GetMapping("purchase-coupon/{id}")
	public boolean purchaseCoupon(HttpSession session, @PathVariable long id) throws CouponSystemException {
		
		customerFromSession = (Customer) session.getAttribute("user");
		Customer customer = customerRepository.findById(customerFromSession.getId());
		Coupon coupon = uploadCouponFromDatabase(id);
		
		if (coupon.getEndDate().before( new Date() )) {
			throw new CouponSystemException("Unable to purchase coupon : coupon is expired");
		}
		if (coupon.getAmount() == 0) {
			throw new CouponSystemException("Unable to purchase coupon : coupon is out of stock");
		}
		
		coupon.subtractOne();
		couponRepository.save(coupon);
		customer.addCoupon(coupon);
		customerRepository.save(customer);
		
		Income income = new Income();
		income.setName(customer.getName());
		income.setDescription(IncomeType.CUSTOMER_PURCHASE);
		income.setAmount( coupon.getPrice() );
		incomeRepository.save(income);
		
		System.out.println(
				"Coupon '" + coupon.getTitle() + "' purchased successfully by customer " + customer.getName());
		return true;
	}
	
	@Override
	@DeleteMapping("remove-coupon/{id}")
	public boolean cancelPurchase(HttpSession session, @PathVariable long id) throws CouponSystemException {
		
		customerFromSession = (Customer) session.getAttribute("user");
		Customer customer = customerRepository.findById(customerFromSession.getId());
		
		Coupon coupon = customer.getCoupon(id);
		uploadCouponFromDatabase(id); //will throw an exception will message "coupon not found" if coupon is null
		
		customer.removeCoupon(coupon);
		customerRepository.save(customer);
		
		System.out.println("Coupon " + coupon.getId() + " removed from customer " + customer.getName());
		return true;
	}
	
	@Override
	@GetMapping("get-coupons")
	public Collection<Coupon> getCoupons(HttpSession session) {
		customerFromSession = (Customer) session.getAttribute("user");
		Customer customer = customerRepository.findById(customerFromSession.getId());
		return customer.getCoupons();
	}
	

	@Override
	@DeleteMapping("remove-all-coupons")
	public boolean removeAllCoupons(HttpSession session)  {
		customerFromSession = (Customer) session.getAttribute("user");
		customerFromSession = customerRepository.findById(customerFromSession.getId());
		customerFromSession.getCoupons().clear();
		customerFromSession = customerRepository.save(customerFromSession);
		
		System.out.println("Removed all coupons from customer " + customerFromSession.getName());
		
		return true;
	}

	@Override
	@GetMapping("get-customer")
	public Customer getCustomer(HttpSession session) {
		customerFromSession = (Customer) session.getAttribute("user");
		Customer customer = customerRepository.findById(customerFromSession.getId());
		CouponSystemUtils.classifyPassword(customer);
		return customer;
	}
	
	private Coupon uploadCouponFromDatabase(long id) throws CouponSystemException {
		Coupon coupon = couponRepository.findById(id);

		if (coupon == null) {
			throw new CouponSystemException("A coupon with the id " + id + " could not be found or does not exist", HttpStatus.NOT_FOUND);
		}

		return coupon;
	}
	
}
