package edu.nus.java_ca.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.nus.java_ca.model.Department;
import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.User;

public interface LeaveService {
	
 
	//for applying
	Leave createLeave(Leave l);
	Leave changeLeave(Leave l);
	public void cancelLeave(Leave l);
	
	//for managers only
	public List<Leave> listLeavesByUserId(Long id);
	ArrayList<Leave> findAppliedLeaves();
	public List<Leave> listAllLeaves();
	public List<Leave> listLeaveToApprove(Department d);
	public void approveLeave(Leave l);
	public void rejectLeave(Leave l);
	public Leave findLeaveById(Long id);
	public List<Leave> findLeavesByYearandMonth(int yy, int mm);	//NEW QUERY

	//for staff
	Page<Leave> findByUser(User u,Pageable p);
	Long countLeaves(LocalDate s, LocalDate e);
	Boolean checkDupes(LocalDate s, LocalDate e, User u);
	Boolean deductleave(Leave l, User u, Integer i);

	//add holiday to the calendar
	void addHoliday(LocalDate d);

	//for pangination
	public List<Leave> listAllLeaves1();
		//public List<Module> listModuleByStudentId(int id);
	List<Leave> getAllLeaves(int pageNo, int pageSize, User u);

}
