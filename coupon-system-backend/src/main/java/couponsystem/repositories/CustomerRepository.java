package couponsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import couponsystem.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	Customer findByName(String name);

	Customer findByNameAndPassword(String name, String password);

	Customer findById(long id);

	void deleteById(long id);
	
}
