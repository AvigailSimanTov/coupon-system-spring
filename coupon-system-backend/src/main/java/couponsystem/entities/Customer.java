package couponsystem.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Customer implements Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	private String name;

	@NotNull
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Coupon> coupons = new ArrayList<>();

	public Customer() {
	}

	public Customer(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}

	public Customer(String name, String password, List<Coupon> coupons) {
		super();
		this.name = name;
		this.password = password;
		this.coupons = coupons;
	}

	public Customer(long id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}
	
	public Customer( long id ) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}
	
	public Coupon getCoupon(long couponId) {
		
		for( Coupon coupon : coupons ) {
			if (couponId == coupon.getId()) {
				return coupon;
			}
		}
		return null;
	}

	public void addCoupon(Coupon coupon) {
		coupons.add(coupon);
	}

	public void removeCoupon(Coupon coupon) {
		for(Coupon couponInList : coupons) {
			if ( couponInList.getId() == coupon.getId() ) {
				coupon = couponInList;
				break;
			}
		}
		
		coupons.remove(coupon);
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + "]";
	}

}
