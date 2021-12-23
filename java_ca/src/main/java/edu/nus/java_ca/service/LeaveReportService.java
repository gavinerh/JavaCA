package edu.nus.java_ca.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveReport;

public interface LeaveReportService {

	List<LeaveReport> convertLeaveToRecord(List<Leave> leaves);
	void exportToCSV(HttpServletResponse response, List<Leave> cachedLeave) throws IOException;
}
