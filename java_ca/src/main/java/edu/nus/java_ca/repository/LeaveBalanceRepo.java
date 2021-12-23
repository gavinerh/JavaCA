
package edu.nus.java_ca.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.User;

public interface LeaveBalanceRepo extends JpaRepository<LeaveBalance, Integer> {
	
	LeaveBalance findByBalId(Integer balId);

	LeaveBalance findTop1ByUserAndLeavetype(User u, String s);
	
	ArrayList<LeaveBalance> findByUser(User u);
	
	List<LeaveBalance> findAll();
	
	List<LeaveBalance> findTop1ByLeavetype(String leavetype);
	
	@Query("SELECT DISTINCT lb.leavetype FROM LeaveBalance lb WHERE lb.leavetype IS NOT NULL")
	List<String> findAllLeaveTypes();
	
	
	@Modifying
	@Query("DELETE FROM LeaveBalance lb WHERE (lb.leavetype = :leavetypename AND lb.user = :user)")
	void deleteLeaveBalanceByType(String leavetypename, User user);
	
	List<LeaveBalance> findByLeavetype(String leavetype);
}
