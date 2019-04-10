package couponsystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import couponsystem.entities.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

	Company findByName(String name);

	Company findByNameAndPassword(String name, String password);

	Company findById(long id);

	void deleteById(long id);
	
}
