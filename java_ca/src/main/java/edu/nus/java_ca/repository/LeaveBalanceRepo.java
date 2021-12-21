
package edu.nus.java_ca.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.User;

public interface LeaveBalanceRepo extends JpaRepository<LeaveBalance, Integer> {
	
	List<LeaveBalance> findByBalId(Long balId);

	LeaveBalance findByUserAndLeavetype(User u, String s);
	
	ArrayList<LeaveBalance> findByUser(User u);
}
