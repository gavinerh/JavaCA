
package edu.nus.java_ca.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.User;

public interface LeaveBalanceRepo extends JpaRepository<LeaveBalance, Integer> {
	
	LeaveBalance findByBalId(Integer balId);

	LeaveBalance findByUserAndLeavetype(User u, String s);
	
	ArrayList<LeaveBalance> findByUser(User u);
	
	List<LeaveBalance> findAll();
	
	List<LeaveBalance> findByLeavetype(String leavetype);
}
