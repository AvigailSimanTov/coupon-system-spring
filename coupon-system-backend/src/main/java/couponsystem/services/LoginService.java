package couponsystem.services;

import javax.servlet.http.HttpSession;

import couponsystem.enums.ClientType;
import couponsystem.exeptions.CouponSystemException;

public interface LoginService {

	boolean login(String name, String password, ClientType clientType, HttpSession session)
			throws CouponSystemException;

}
