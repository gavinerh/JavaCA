package edu.nus.java_ca.model;

import java.time.LocalDate;

public class LeaveReport {

	private Long leaveId;
	private String email;
	private LocalDate startDate;
	private LocalDate endDate;
	private LeaveStatus status;
	private String leaveType;
	private String workDiss;
	private String contactDetails;
	private String reason;
	
	public LeaveReport(Long leaveId, String email, LocalDate startDate, LocalDate endDate, LeaveStatus status,
			String leaveType, String workDiss, String contactDetails, String reason) {
		super();
		this.leaveId = leaveId;
		this.email = email;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.leaveType = leaveType;
		this.workDiss = workDiss;
		this.contactDetails = contactDetails;
		this.reason = reason;
	}

	public Long getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(Long leaveId) {
		this.leaveId = leaveId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LeaveStatus getStatus() {
		return status;
	}

	public void setStatus(LeaveStatus status) {
		this.status = status;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}



	public String getWorkDiss() {
		return workDiss;
	}

	public void setWorkDiss(String workDiss) {
		this.workDiss = workDiss;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}
	
	
}
