package couponsystem.services;

import java.util.Collection;
import java.util.List;

import couponsystem.entities.Coupon;
import couponsystem.enums.CouponType;
import couponsystem.exeptions.CouponSystemException;

public interface CouponService {

	Coupon getCoupon(long id) throws CouponSystemException;
	
	Collection<Coupon> getAllCoupons() throws CouponSystemException;
	
	List<Coupon> getCouponsByType(CouponType type) throws CouponSystemException;

	List<Coupon> getCouponsByTitle(String title) throws CouponSystemException;

	List<Coupon> getCouponsByKeyword(String keyword) throws CouponSystemException;
	

}
