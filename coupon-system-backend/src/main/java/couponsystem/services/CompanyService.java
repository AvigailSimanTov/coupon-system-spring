package couponsystem.services;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import couponsystem.entities.Company;
import couponsystem.entities.Coupon;
import couponsystem.exeptions.CouponSystemException;

public interface CompanyService {

	long createCoupon(HttpSession sesssion, Coupon coupon) throws CouponSystemException;
	
	boolean removeCoupon(HttpSession session, long id) throws CouponSystemException;
	
	boolean updateCoupon(HttpSession session, Coupon coupon) throws CouponSystemException;
	
	Coupon getCoupon(HttpSession session, long id) throws CouponSystemException;
	
	Collection<Coupon> getCoupons(HttpSession session) throws CouponSystemException;
	
	Company getCompany(HttpSession session) throws CouponSystemException;

}
