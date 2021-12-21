package edu.nus.java_ca.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import edu.nus.java_ca.model.Department;
import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveStatus;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.LeaveRepo;
import edu.nus.java_ca.repository.UserRepository;

@Service //need to impl component type to be recognized as spring bean
public class LeaveServiceImpl implements LeaveService{
 
	@Autowired
	LeaveRepo lrepo;
	
	final Set<String> weekends = Set.of("SATURDAY","SUNDAY");
	//Set of holidays in singapore 2021
	final Set<LocalDate> holidays = Set.of(LocalDate.of(2021,1,1),LocalDate.of(2021,2,12),LocalDate.of(2021,2,13),LocalDate.of(2021,4,2),
			LocalDate.of(2021,5,1),LocalDate.of(2021,5,13),LocalDate.of(2021,2,26),LocalDate.of(2021,7,20),LocalDate.of(2021,8,9),
			LocalDate.of(2021,11,4),LocalDate.of(2021,12,25));
	
	@Autowired
	UserRepository uRepo;

	//Base function	
	@Transactional
	public List<Leave> listAllLeaves() {
		return lrepo.findAll();
	}
	
	@Override
	@Transactional
	public Leave createLeave(Leave l) {
		// TODO Auto-generated method stub
		return lrepo.saveAndFlush(l);
	}

	@Override
	@Transactional
	public Leave changeLeave(Leave l) {
		// TODO Auto-generated method stub
		return lrepo.saveAndFlush(l);
	}
	
	@Transactional
	public void cancelLeave(Leave l) {
		l.setStatus(LeaveStatus.CANCELLED);
		lrepo.save(l);		
	}
	
	@Transactional
	public List<Leave> findLeavesByDate(LocalDate d) {
		List<Leave> mthLeave = lrepo.findLeaveByDate(d);
		return mthLeave;
	}
	
	//Manager's function
	@Transactional
	public List<Leave> listLeavesByUserId(Long id) {
		
	 User user1 = uRepo.findByUserId(id);
		
		List<Leave> EmplLeave = lrepo.findLeaveByUser(user1);
		return EmplLeave;
	}

	@Override
	@Transactional
	public ArrayList<Leave> findAppliedLeaves() {
		// TODO Auto-generated method stub
		return lrepo.findAppliedLeaves();
	}
	
	@Transactional
	public List<Leave> listLeaveToApprove(Department d) {
		List<Leave> LeavetoApprove = lrepo.findLeaveToApprove(LeaveStatus.APPLIED, 
				LeaveStatus.UPDATED,d);
		return LeavetoApprove;
	}	
	@Modifying //if its CUD need to inform server its not a readonly query
	@Transactional
	public void approveLeave(Leave l) {
		l.setStatus(LeaveStatus.APPROVED);
		lrepo.save(l);
	}
	@Modifying 
	@Transactional	
	public void rejectLeave(Leave l) {
		l.setStatus(LeaveStatus.REJECTED);
		lrepo.save(l);
	}
	
	@Transactional
	public Leave findLeaveById(Long id) {
		return lrepo.findLeaveById(id);
	}

	@Transactional
	public Long countLeaves(LocalDate s, LocalDate e) {
		Long count;
		count = s.datesUntil(e.plusDays(1))
				.count();

		if(count<=14) {
			count = s.datesUntil(e.plusDays(1))
			        .filter(t -> !weekends.contains(t.getDayOfWeek().name()))
			        .filter(t -> !holidays.contains(t))
			        .count();
		}
		return count;
	}
	@Override
	@Transactional
	public Leave findByStartDateAndEndDate(LocalDate s, LocalDate e) {
		// TODO Auto-generated method stub
		return lrepo.findByStartDateAndEndDate(s, e);
	}
	@Transactional
	public Boolean checkDupes(LocalDate s, LocalDate e) {
		Leave c = findByStartDateAndEndDate(s,e);
		if(c!=null) {
			return true;}
		
		return false;
	}
}
