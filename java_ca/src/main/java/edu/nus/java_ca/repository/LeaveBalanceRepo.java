
package edu.nus.java_ca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.nus.java_ca.model.LeaveBalance;

public interface LeaveBalanceRepo extends JpaRepository<LeaveBalance, Integer> {
	List<LeaveBalance> findByBalId(Long balId);
	
	List<LeaveBalance> findAll();

}
