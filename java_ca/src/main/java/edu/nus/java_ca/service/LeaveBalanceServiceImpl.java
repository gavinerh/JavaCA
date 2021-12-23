package edu.nus.java_ca.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.LeaveBalanceRepo;
@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

	@Autowired
	LeaveBalanceRepo lbrepo;
	
//	@Override
//	@Transactional
//	public List<LeaveBalance> findByBalId(Long balId) {
//		// TODO Auto-generated method stub
//		return lbrepo.findByBalId(balId);
//	}

	@Override
	@Transactional
	public LeaveBalance findByUserAndLeavetype(User u, String s) {
		// TODO Auto-generated method stub
		return lbrepo.findTop1ByUserAndLeavetype(u, s);
	}

	@Override
	@Transactional
	public List<LeaveBalance> findall() {
		// TODO Auto-generated method stub
		return lbrepo.findAll();
	}
	@Transactional
	public void saveLeaveBalance(LeaveBalance lb) {
		lbrepo.saveAndFlush(lb);
	}

	@Override
	public ArrayList<LeaveBalance> findByUser(User u) {
		// TODO Auto-generated method stub
		return lbrepo.findByUser(u);
	}

	@Override
	public List<LeaveBalance> findByLeavetype(String leavetype) {
		// TODO Auto-generated method stub
		return lbrepo.findTop1ByLeavetype(leavetype);
	}

	@Override
	public LeaveBalance findByBalId(Integer id) {
		// TODO Auto-generated method stub
		return lbrepo.findByBalId(id);
	}
	
	@Override
	public List<String> findAllLeaveTypes() {
		// TODO Auto-generated method stub
		return lbrepo.findAllLeaveTypes();
	}
	
	@Modifying
	@Transactional
	public void deleteLeaveBalanceByType(String leavetypename, User user) {
		// TODO Auto-generated method stub
		lbrepo.deleteLeaveBalanceByType(leavetypename, user);
	}
	
	@Override
	public List<LeaveBalance> findByLeavetype2(String leavetype) {
		// TODO Auto-generated method stub
		return lbrepo.findByLeavetype(leavetype);
	}


	

}
