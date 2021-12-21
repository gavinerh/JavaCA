package edu.nus.java_ca.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveStatus;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.LeaveRepo;
import edu.nus.java_ca.repository.UserRepository;

@Service //need to impl component type to be recognized as spring bean
public class LeaveServiceImpl implements LeaveService{
 
	@Autowired
	LeaveRepo lrepo;
	
	
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
		
	//Movement Register
//	@Transactional
//	public List<Leave> findLeavesByDate(LocalDate d) {
//		List<Leave> LeavesbyDate = lrepo.findLeaveByDate(d);
//		return LeavesbyDate;
//	}
	@Transactional
	public List<Leave> findLeavesByYearandMonth(int yy, int mm){
		List<Leave> LeavesByMMYY = lrepo.getByYearandMonth(yy, mm);
		return LeavesByMMYY;			
	}
	
	//Manager's function
//	@Transactional
//	public List<Leave> listLeavesByUserId(Long id) {
//		
//	 User user1 = uRepo.findByUserId(id);
//		
//		List<Leave> EmplLeave = lrepo.findLeaveByUser(user1);
//		return EmplLeave;
//	}

	@Transactional
	public List<Leave> listLeavesByUserId(Long id) {
		List<Leave> EmplLeave = lrepo.findLeaveByUserId(id);
		return EmplLeave;
	}
	
	@Override
	@Transactional
	public ArrayList<Leave> findAppliedLeaves() {
		// TODO Auto-generated method stub
		return lrepo.findAppliedLeaves();
	}
	
	@Transactional
	public List<Leave> listLeaveToApprove() {
		List<Leave> LeavetoApprove = lrepo.findLeaveToApprove(LeaveStatus.APPLIED, 
				LeaveStatus.UPDATED);
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

	
}
