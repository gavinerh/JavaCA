package edu.nus.java_ca.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
		return lbrepo.findByUserAndLeavetype(u, s);
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
		return lbrepo.findByLeavetype(leavetype);
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
	

}
