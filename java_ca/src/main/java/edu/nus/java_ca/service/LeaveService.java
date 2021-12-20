package edu.nus.java_ca.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.nus.java_ca.model.Leave;

public interface LeaveService {
	
 
	//for applying
	Leave createLeave(Leave l);
	Leave changeLeave(Leave l);
	public void cancelLeave(Leave l);
	
	//for managers only
	public List<Leave> listLeavesByUserId(Long id);
	ArrayList<Leave> findAppliedLeaves();
	public List<Leave> listAllLeaves();
	public List<Leave> listLeaveToApprove();
	public void approveLeave(Leave l);
	public void rejectLeave(Leave l);
	public Leave findLeaveById(Integer id);
	public List<Leave> findLeavesByDate(LocalDate d);
	//for staff
	Leave findByStartDateAndEndDate(LocalDate s, LocalDate e);
	Long countLeaves(LocalDate s, LocalDate e);
	Boolean checkDupes(LocalDate s, LocalDate e);
}
