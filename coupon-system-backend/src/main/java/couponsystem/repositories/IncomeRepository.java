package couponsystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import couponsystem.entities.Income;
import couponsystem.enums.IncomeType;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Integer> {

	List<Income> findByName(String name);
	
	List<Income> findByDescription(IncomeType description);
	
	List<Income> findByNameAndDescription(String name, IncomeType description);
	
}
