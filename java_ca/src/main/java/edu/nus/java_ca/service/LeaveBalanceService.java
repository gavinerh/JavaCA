package edu.nus.java_ca.service;

import java.util.ArrayList;
import java.util.List;

import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.TypesOfLeave;
import edu.nus.java_ca.model.User;

public interface LeaveBalanceService {
	
	//List<LeaveBalance> findByBalId(Long balId);

	LeaveBalance findByUserAndLeavetype(User u, String s);
	
	List<LeaveBalance> findall();
	
	ArrayList<LeaveBalance> findByUser(User u);
	
	List<LeaveBalance> findByLeavetype(String leavetype);
	
	LeaveBalance findByBalId(Integer id);
	
	void saveLeaveBalance(LeaveBalance lb);
	
	List<String> findAllLeaveTypes();
	
	void deleteLeaveBalanceByType(String leavetypename, User user);
	
	List<LeaveBalance> findByLeavetype2(String leavetype);
	
	List<TypesOfLeave> findDistinctLeaveType();

}
