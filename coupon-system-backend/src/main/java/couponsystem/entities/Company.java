package couponsystem.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Company implements Client {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String name;
	
	@NotNull
	private String password;
	
	@NotNull
	private String email;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "company", fetch=FetchType.EAGER)
	private List<Coupon> coupons = new ArrayList<>();
	
	public Company() {	}

	public Company(String name, String password, String email) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
	}

	public Company(String name, String password, String email, List<Coupon> coupons) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
		this.coupons = coupons;
	}

	public Company(long id, String name, String password, String email) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.email = email;
	}
	
	public Company( long id ) {
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}
	
	public Coupon getCoupon(long couponId) {
		for (Coupon coupon : coupons) {
			
			if(coupon.getId() == couponId) {
				return coupon;
			}
		}
		return null;
	}

	public void addCoupon( Coupon coupon ) {
		coupons.add(coupon);
	}
	
	public void removeCoupon(Coupon coupon ) {
		coupons.remove(coupon);
	}
	
	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + "]";
	}

}
