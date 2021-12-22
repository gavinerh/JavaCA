package edu.nus.java_ca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.ReportRepository;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	ReportRepository rRepo;
	
	@Override
	public List<Leave> findLeaveByApprovingOfficer(User manager) {
		return rRepo.findLeaveByApprovingOfficer(manager);
	}

}
