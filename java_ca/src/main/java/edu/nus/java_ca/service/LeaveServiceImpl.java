package edu.nus.java_ca.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.iss.laps.model.Leave;
import sg.iss.laps.model.LeaveStatus;
import sg.iss.laps.repository.LeaveRepo;

@Service //need to impl component type to be recognized as spring bean
public class LeaveServiceImpl implements LeaveService{

	@Autowired
	LeaveRepo lrepo;

	//Base function	
	@Transactional
	public void addLeave(Leave l) {
		lrepo.save(l);
	}
	
	@Transactional
	public void cancelLeave(Leave l) {
		l.setStatus(LeaveStatus.CANCELLED);
		lrepo.save(l);		
	}
	
	//Manager's function
	@Transactional
	public List<Leave> findLeaveByUserId(Long id) {
		List<Leave> EmplLeave = lrepo.findLeaveByUser_UserIdLike(id);
		return EmplLeave;
	}
	
	@Transactional
	public List<Leave> listLeaveToApprove() {
		List<Leave> LeavetoApprove = lrepo.findLeaveToApprove(LeaveStatus.APPLIED, 
				LeaveStatus.UPDATED);
		return LeavetoApprove;
	}	
	@Transactional
	public void approveLeave(Leave l) {
		l.setStatus(LeaveStatus.APPROVED);
		lrepo.save(l);
	}
	@Transactional	
	public void rejectLeave(Leave l) {
		l.setStatus(LeaveStatus.REJECTED);
		lrepo.save(l);
	}
	@Transactional
	public Leave findLeaveById(Integer id) {
		return lrepo.findById(id).get();
	}

	
}
