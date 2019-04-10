package couponsystem.services.restImplementations;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
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

import couponsystem.entities.Company;
import couponsystem.entities.Coupon;
import couponsystem.entities.Customer;
import couponsystem.entities.Income;
import couponsystem.enums.IncomeType;
import couponsystem.exeptions.CouponSystemException;
import couponsystem.repositories.CompanyRepository;
import couponsystem.repositories.CouponRepository;
import couponsystem.repositories.CustomerRepository;
import couponsystem.repositories.IncomeRepository;
import couponsystem.services.CompanyService;
import couponsystem.utilities.CouponSystemUtils;

@RestController
@RequestMapping("secure/company")
public class CompanyServiceImplementation implements CompanyService {

	public static final double COMPANY_NEW_COUPON_PRICE = 100;
	public static final double COMPANY_UPDATE_COUPON_PRICE = 10;
	private static final int MINIMUM_TITLE_LENGTH = 3;
	private static final int MAXIMUM_TITLE_LENGTH = 25;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CouponRepository couponRepository;
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private IncomeRepository incomeRepository;

	Company companyFromSession;

	@Override
	@Transactional
	@PostMapping("create-coupon")
	public long createCoupon(HttpSession session, @RequestBody Coupon coupon) throws CouponSystemException {

		validateFields(coupon);
		
		companyFromSession = (Company) session.getAttribute("user");
		coupon.setCompany(companyFromSession);
		coupon = couponRepository.save(coupon);

		Income income = new Income();
		income.setName(companyFromSession.getName());
		income.setDescription(IncomeType.COMPANY_NEW_COUPON);
		income.setAmount(COMPANY_NEW_COUPON_PRICE);
		incomeRepository.save(income);

		System.out.println("Coupon " + coupon.getTitle() + " created for " + companyFromSession.getName());

		return coupon.getId();
	}

	@Override
	@Transactional
	@DeleteMapping("remove-coupon/{id}")
	public boolean removeCoupon(HttpSession session, @PathVariable long id) throws CouponSystemException {
		companyFromSession = (Company) session.getAttribute("user");
		
		List<Customer> customers = customerRepository.findAll();
		for (Customer customer : customers) {
			List<Coupon> coupons = customer.getCoupons();
			for (Coupon coupon : coupons) {
				if (coupon.getId() == id) {
					throw new CouponSystemException("Can't remove coupon after it has been purchased by customers!", HttpStatus.FORBIDDEN);
				}
			}
		}
		
		Coupon coupon = uploadCouponFromDatabase(id);
		denyAccessIfWrongCompany(coupon, companyFromSession);

		couponRepository.deleteById(id);

		if (couponRepository.findById(id) != null) {
			throw new CouponSystemException("Error removing coupon '" + coupon.getTitle() + "'");
		}

		System.out.println("Coupon '" + coupon.getTitle() + "' removed");
		return true;
	}

	@Override
	@Transactional
	@PutMapping("update-coupon")
	public boolean updateCoupon(HttpSession session, @RequestBody Coupon coupon) throws CouponSystemException {

		companyFromSession = (Company) session.getAttribute("user");
		
		Coupon existingCoupon = uploadCouponFromDatabase(coupon.getId());
		denyAccessIfWrongCompany(existingCoupon, companyFromSession);
		validateFields(coupon);
		coupon.setCompany(existingCoupon.getCompany());

		couponRepository.save(coupon);
		
		Income income = new Income();
		income.setName(companyFromSession.getName());
		income.setDescription(IncomeType.COMPANY_UPDATE_COUPON);
		income.setAmount(COMPANY_UPDATE_COUPON_PRICE);
		incomeRepository.save(income);
		
		System.out.println("Coupon " + coupon.getTitle() + " updated for " + companyFromSession.getName());
		return true;

	}

	@Override
	@GetMapping("get-coupon/{id}")
	public Coupon getCoupon(HttpSession session, @PathVariable long id) throws CouponSystemException {
		
		companyFromSession = (Company) session.getAttribute("user");
		Coupon coupon = uploadCouponFromDatabase(id);
		denyAccessIfWrongCompany(coupon, companyFromSession);
		
		return coupon;
	}

	@Override
	@GetMapping("get-coupons")
	public Collection<Coupon> getCoupons(HttpSession session) {
		companyFromSession = (Company) session.getAttribute("user");
		Company company = companyRepository.findById(companyFromSession.getId());
		return company.getCoupons();
	}

	@Override
	@GetMapping("get-company")
	public Company getCompany(HttpSession session) {
		companyFromSession = (Company) session.getAttribute("user");
		Company company = companyRepository.findById(companyFromSession.getId());
		CouponSystemUtils.classifyPassword(company);
		return company;
	}

	// VALIDATIONS :
	
	private Coupon uploadCouponFromDatabase(long id) throws CouponSystemException {
		Coupon coupon = couponRepository.findById(id);

		if (coupon == null) {
			throw new CouponSystemException("A coupon with the id " + id + " could not be found or does not exist", HttpStatus.NOT_FOUND);
		}

		return coupon;
	}
	
	private void denyAccessIfWrongCompany ( Coupon coupon, Company company ) throws CouponSystemException {
		if (coupon.getCompany().getId() != company.getId()) {
			throw new CouponSystemException("Coupon does not belong to company - access denied.", HttpStatus.FORBIDDEN);
		}
	}
	
	private void validateFields( Coupon coupon ) throws CouponSystemException {
		
		if ( coupon.getTitle() == null || coupon.getStartDate() == null || coupon.getEndDate() == null || coupon.getType() == null || coupon.getMessage() == null ) {
			throw new CouponSystemException("Unable to complete action - missing fields");
		}
		
		if (!CouponSystemUtils.between(coupon.getTitle().length(), MINIMUM_TITLE_LENGTH, MAXIMUM_TITLE_LENGTH)) {
			throw new CouponSystemException("Coupon title must contain " + MINIMUM_TITLE_LENGTH + " to " + MAXIMUM_TITLE_LENGTH + " characters and must not be empty.");
		}
		
		Date today = new Date();
		if (coupon.getEndDate().before(today) ) {
			throw new CouponSystemException("Coupon end date must not be expired");
		}
		
		if (coupon.getEndDate().before(coupon.getStartDate())) {
			throw new CouponSystemException("Oops! Please make sure that the relese date is before the expiration date.");
		}
		
	}
	
}
