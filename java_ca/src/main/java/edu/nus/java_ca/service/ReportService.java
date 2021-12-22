package edu.nus.java_ca.service;

import java.util.List;

import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.User;

public interface ReportService {

	List<Leave> findLeaveByApprovingOfficer(User manager);
}
