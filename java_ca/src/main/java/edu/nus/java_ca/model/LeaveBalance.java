package edu.nus.java_ca.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "leavebal")
public class LeaveBalance {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer balId;
	private String leavetype;
	private Integer balance;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid")
	private User user;

	public LeaveBalance() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LeaveBalance(Integer balId, String leavetype, Integer balance, User user) {
		super();
		this.balId = balId;
		this.leavetype = leavetype;
		this.balance = balance;
		this.user = user;
	}

	public Integer getBalId() {
		return balId;
	}

	public void setBalId(Integer balId) {
		this.balId = balId;
	}

	public String getLeavetype() {
		return leavetype;
	}

	public void setLeavetype(String leavetype) {
		this.leavetype = leavetype;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(balId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LeaveBalance other = (LeaveBalance) obj;
		return Objects.equals(balId, other.balId);
	}
	
	public LeaveBalance(String leavetype, Integer balance, User user) {
		super();
		this.leavetype = leavetype;
		this.balance = balance;
		this.user = user;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s;
		return s = "Leave Type: "+this.leavetype+"\nLeave Balance: "+this.balance;
	}
	

}
