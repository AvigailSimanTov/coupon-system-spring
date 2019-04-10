package couponsystem.utilities;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import couponsystem.entities.Coupon;
import couponsystem.repositories.CouponRepository;

// Runs once every 24 hours, removes expired coupons
@Component
public class DailyCouponExpirationTask extends Thread {

	@Autowired
	private CouponRepository couponRepository;
	
	private Date startDate;
	private boolean running;

	public DailyCouponExpirationTask() {
		this.running = true;
	}

	@Override
	public void run() {
		if (running) {
			System.out.println("Daily expired coupon remover initiated");
			ExpirationTask();
		}
	}

	private void ExpirationTask() {
		startDate = new Date();
		Collection<Coupon> allCoupons = couponRepository.findAll();
		int i = 0;
		for (Coupon coupon : allCoupons) {
			if (coupon.getEndDate().before(startDate) || coupon.getAmount() == 0) {
				couponRepository.delete(coupon);
				i++;
			} else {
			}
		}
		System.out.println(i + " Coupons removed");
		try {
			TimeUnit.HOURS.sleep(24);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void stopTask() {
		running = false;
	}

}
