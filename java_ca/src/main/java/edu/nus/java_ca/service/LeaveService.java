package edu.nus.java_ca.service;

import java.util.List;

import sg.iss.laps.model.Leave;

public interface LeaveService {
	
	//for applying
	public void addLeave(Leave l);
	public void cancelLeave(Leave l);
	
	//for managers only
	public List<Leave> listLeaveToApprove();
	public void approveLeave(Leave l);
	public void rejectLeave(Leave l);
	public Leave findLeaveById(Integer id);
	
}
