package edu.nus.java_ca.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import edu.nus.java_ca.model.Leave;
import edu.nus.java_ca.model.LeaveReport;

@Service
public class LeaveReportServiceImpl implements LeaveReportService {

	@Override
	public List<LeaveReport> convertLeaveToRecord(List<Leave> leaves) {
		List<LeaveReport> lrList = new ArrayList<LeaveReport>();
		for (Leave l : leaves) {
			// LeaveReport lr = new LeaveReport(l.getLeaveId(), l.getUser().getEmail(),
			// l.getStartDate(), l.getEndDate(), l.getStatus(), l.getType(),
			// l.getWorkdissem(), l.getContactdetail());
			lrList.add(new LeaveReport(l.getLeaveId(), l.getUser().getEmail(), l.getStartDate(), l.getEndDate(),
					l.getStatus(), l.getType(), l.getWorkdissem(), l.getContactdetail(), l.getReason()));
		}
		return lrList;
	}

	public void exportToCSV(HttpServletResponse response, List<Leave> cachedLeave) throws IOException{
		response.setContentType("text/csv");
		String filename = "leave.csv";
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + filename;
		
		//set header to response
		response.setHeader(headerKey, headerValue);
		List<LeaveReport> toCsvList = null;
		
		toCsvList = convertLeaveToRecord(cachedLeave);
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		// header line
		String[] csvHeader = {"Leave Id", "Contact Detail", "End Date", "Start Date", "Leave Status", "Leave Type", "Work dissemination", "User Email", "Reason"};
		String[] nameMapping = {"leaveId", "contactDetails" ,"endDate", "startDate", "status", "leaveType", "workDiss", "email", "reason"};
		
		csvWriter.writeHeader(csvHeader);
		for(LeaveReport leave : toCsvList) {
			csvWriter.write(leave, nameMapping);
		}
		
		csvWriter.close();
	}

	
}
