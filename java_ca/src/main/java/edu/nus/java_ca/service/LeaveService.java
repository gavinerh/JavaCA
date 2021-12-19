package edu.nus.java_ca.service;

import java.util.ArrayList;
import java.util.List;

import edu.nus.java_ca.model.Leave;

public interface LeaveService {
	
	//for applying
	public void addLeave(Leave l);
	public void cancelLeave(Leave l);
	
	//for managers only
	public List findLeaveByUserId(Long id);
	public List<Leave> listAllLeaves();
	public List<Leave> listLeaveToApprove();
	public void approveLeave(Leave l);
	public void rejectLeave(Leave l);
	public Leave findLeaveById(Integer id);
	ArrayList<Leave> findAppliedLeaves();
	Leave createLeave(Leave l);
	Leave changeLeave(Leave l);
	
}
