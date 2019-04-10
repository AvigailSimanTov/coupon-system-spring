package couponsystem.utilities;

import java.util.Comparator;

import couponsystem.entities.Coupon;

public class CouponComparator implements Comparator<Coupon> {

	public enum CouponSortingType {
		TITLE, TYPE, PRICE
	}

	private CouponSortingType sortingBy = CouponSortingType.TITLE;

	@Override
	public int compare(Coupon coupon1, Coupon coupon2) {
		
		switch (sortingBy) {
		case TITLE:
			return coupon1.getTitle().compareTo(coupon2.getTitle());
		case TYPE:
			return coupon1.getType().compareTo(coupon2.getType());
		case PRICE:
			double double1 = coupon1.getPrice();
			double double2 = (int) coupon1.getPrice();
			String price1 = Double.toString(double1);
			String price2 = Double.toString(double2);
			return price1.compareTo(price2);
		}
		throw new RuntimeException("An unexpected error occured, please make sure to sort by a valid argument.");
	}

	public void setSortingBy(CouponSortingType sortingBy) {
		this.sortingBy = sortingBy;
	}

}