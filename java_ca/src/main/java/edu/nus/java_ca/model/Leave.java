package edu.nus.java_ca.model;


import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name ="leave2")
public class Leave {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long leaveId;
	//uni directional so dont need to specify in User
	@ManyToOne
	private User user;
	@Column(name = "status", columnDefinition = "ENUM('APPLIED', 'APPROVED', 'UPDATED', 'CANCELLED', 'REJECTED','DELETED')")
	@Enumerated(EnumType.STRING)
	private LeaveStatus status;	//enum
	private String reason;
	private String workdissem;
	private String type;
	private String contactdetail;
	
	@Column(name = "applieddate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate appliedDate;
	
	@Column(name = "startdate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	@Column(name = "enddate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	
	public Leave() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Leave(LeaveStatus ls) {
		super();
		this.status=ls;
	}
	public Leave(User user, LeaveStatus status, String type, String reason, String workdissem,
			 LocalDate appliedDate,
			 LocalDate startDate,
			 LocalDate endDate) {
		super();
		this.user = user;
		this.status = status;
		this.type=type;
		this.reason = reason;
		this.workdissem = workdissem;
		this.appliedDate = appliedDate;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	public Leave(Long leaveId, User user, LeaveStatus status, String type, String reason, String workdissem,
			LocalDate appliedDate,
			LocalDate startDate,
			LocalDate endDate) {
		super();
		this.leaveId = leaveId;
		this.user = user;
		this.status = status;
		this.type=type;
		this.reason = reason;
		this.workdissem = workdissem;
		this.appliedDate = appliedDate;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Long getLeaveId() {
		return leaveId;
	}
	public void setLeaveId(Long leaveId) {
		this.leaveId = leaveId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public LeaveStatus getStatus() {
		return status;
	}
	public void setStatus(LeaveStatus status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getWorkdissem() {
		return workdissem;
	}
	public void setWorkdissem(String workdissem) {
		this.workdissem = workdissem;
	}
	public LocalDate getAppliedDate() {
		return appliedDate;
	}
	public void setAppliedDate(LocalDate appliedDate) {
		this.appliedDate = appliedDate;
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
	public String getContactdetail() {
		return contactdetail;
	}
	public void setContactdetail(String contactdetail) {
		this.contactdetail = contactdetail;
	}
	@Override
	public int hashCode() {
		return Objects.hash(leaveId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Leave other = (Leave) obj;
		return Objects.equals(leaveId, other.leaveId);
	}
	@Override
	public String toString() {
		return "Leave [leaveId=" + leaveId + ", user=" + user + ", status=" + status + ", reason=" + reason
				+ ", workdissem=" + workdissem + ", type=" + type + ", contactdetail=" + contactdetail
				+ ", appliedDate=" + appliedDate + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
	

	



	
}
