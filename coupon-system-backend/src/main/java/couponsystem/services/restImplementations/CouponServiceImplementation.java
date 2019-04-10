package couponsystem.services.restImplementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import couponsystem.entities.Coupon;
import couponsystem.enums.CouponType;
import couponsystem.exeptions.CouponSystemException;
import couponsystem.repositories.CouponRepository;
import couponsystem.services.CouponService;

@RestController
@RequestMapping("coupon")
public class CouponServiceImplementation implements CouponService {

	@Autowired
	CouponRepository repository;

	@Override
	@GetMapping("get-all")
	public List<Coupon> getAllCoupons() {
		return repository.findAll();
	}

	@Override
	@GetMapping("get-by-id/{id}")
	public Coupon getCoupon(@PathVariable long id) throws CouponSystemException {
		
		Coupon coupon = repository.findById(id);
		
		if ( coupon == null ) {
			throw new CouponSystemException("A coupon with the id " + id + " could not be found or does not exist");
		}
		
		return coupon;
	}

	@Override
	@GetMapping("get-by-title/{title}")
	public List<Coupon> getCouponsByTitle(@PathVariable String title) {
		return repository.findByTitle(title);
	}

	@Override
	@GetMapping("get-by-type/{type}")
	public List<Coupon> getCouponsByType(@PathVariable CouponType type) {
		return repository.findByType(type);
	}

	@Override
	@GetMapping("get-by-keyword/{keyword}")
	public List<Coupon> getCouponsByKeyword(@PathVariable String keyword) {
		return repository.findByTitleContaining(keyword);
	}
	
}
