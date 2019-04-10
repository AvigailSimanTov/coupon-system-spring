package couponsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import couponsystem.entities.Coupon;
import couponsystem.enums.CouponType;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
	
	List<Coupon> findByTitle(String title);

	List<Coupon> findByTitleContaining(String title);

	List<Coupon> findByType(CouponType couponType);

	Coupon findById(long id);

	void deleteById(long id);
	
}
