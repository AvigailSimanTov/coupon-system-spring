package couponsystem.services;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import couponsystem.entities.Coupon;
import couponsystem.entities.Customer;
import couponsystem.exeptions.CouponSystemException;

public interface CustomerService {
	
	boolean purchaseCoupon(HttpSession session, long id) throws CouponSystemException;
	
	boolean cancelPurchase(HttpSession session, long id) throws CouponSystemException;

	Collection<Coupon> getCoupons(HttpSession session) throws CouponSystemException;

	boolean removeAllCoupons(HttpSession session) throws CouponSystemException;
	
	Customer getCustomer(HttpSession session) throws CouponSystemException;

}
